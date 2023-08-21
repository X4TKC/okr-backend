package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.upb.okrbackend.entities.ObjectiveEntity;
import com.upb.okrbackend.models.KeyResult;
import com.upb.okrbackend.models.Objective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class KeyResultService {
    private Firestore dbFirestore;
    private String collection="KeyResult";

    public KeyResultService() {
        this.dbFirestore = FirestoreClient.getFirestore();
    }
    public KeyResultService(Firestore dbFirestore){
        this.dbFirestore=dbFirestore;
    }

    public KeyResult getKeyResult(String id) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection(collection).document(id);
        DocumentSnapshot document = documentReference.get().get();
        return setKeyResultFromDocument(document);
    }

    public String createKeyResult(KeyResult keyResult) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> collectionApiFuture = dbFirestore.collection(collection).add(keyResult);
        keyResult.setId(collectionApiFuture.get().getId());
        updateKeyResult(keyResult);
        //ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(keyResult.getId()).set(keyResult);
        addKeyResultToObjective(keyResult.getId(),keyResult.getObjectiveId());
        return Objects.requireNonNull(collectionApiFuture.get().get().get().getUpdateTime()).toString();
    }

    public String updateKeyResult(KeyResult keyResult) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(keyResult.getId()).set(keyResult);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String deleteKeyResult(String id) {
        ApiFuture<WriteResult> writeResult = dbFirestore.collection(collection).document(id).delete();
        return "Successfully deleted " + id;
    }

    public List<KeyResult> getKeyResultListById(DocumentSnapshot documentSnapshot) throws ExecutionException, InterruptedException {
        List<DocumentReference> documentReferenceKeyResult = (List<DocumentReference>) documentSnapshot.getData().get("keyResultList");
        List<KeyResult> keyResultList = new ArrayList<>();
        for (DocumentReference documentReferenceVar : documentReferenceKeyResult
        ) {
            DocumentSnapshot  documentKeyResult = dbFirestore.collection(collection).document(documentReferenceVar.getId()).get().get();
            keyResultList.add(setKeyResultFromDocument(documentKeyResult));
        }
        return keyResultList;
    }
    public void addKeyResultToObjective(String id, String objectiveId) throws ExecutionException, InterruptedException {
        DocumentSnapshot documentSnapshotObjective = dbFirestore.collection("Objective").document(objectiveId).get().get();
        List<DocumentReference> documentReferenceListKeyResult = (List<DocumentReference>) documentSnapshotObjective.getData().get("keyResultList");
        documentReferenceListKeyResult.add(dbFirestore.collection(collection).document(id));
        ObjectiveEntity objectiveEntity = new ObjectiveEntity();
        objectiveEntity.setKeyResultList(documentReferenceListKeyResult);
        objectiveEntity.setId(documentSnapshotObjective.getId());
        objectiveEntity.setDateStart(documentSnapshotObjective.getData().get("dateStart").toString());
        objectiveEntity.setDateEnd(documentSnapshotObjective.getData().get("dateEnd").toString());
        objectiveEntity.setUserId(documentSnapshotObjective.getData().get("userId").toString());
        objectiveEntity.setName(documentSnapshotObjective.getData().get("name").toString());
        dbFirestore.collection("Objective").document(objectiveEntity.getId()).set(objectiveEntity);
    }
    public List<KeyResult> getAllKeyResultsFromObjective(String objectiveId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> queryDocumentSnapshotList = dbFirestore.collection(collection).get().get().getDocuments();
        List<QueryDocumentSnapshot> queryDocumentSnapshot=queryDocumentSnapshotList.stream().filter(a-> a.getData().get("objectiveId").equals(objectiveId)).toList();
        List<KeyResult> keyResultList = new ArrayList<>();
        for (QueryDocumentSnapshot queryDocumentSnapshotVar:queryDocumentSnapshot
        ) {
            keyResultList.add(setKeyResultFromDocument(queryDocumentSnapshotVar));
        }
        return keyResultList;
    }
    public KeyResult checkKeyResult(String id) throws ExecutionException, InterruptedException {
        KeyResult keyResult=this.getKeyResult(id);
        keyResult.setCheck(true);
        this.updateKeyResult(keyResult);
        return keyResult;
    }
    public KeyResult unCheckKeyResult(String id) throws ExecutionException, InterruptedException {
        KeyResult keyResult=this.getKeyResult(id);
        keyResult.setCheck(false);
        this.updateKeyResult(keyResult);
        return keyResult;
    }

    public void deleteAllKeyResultFromObjective(String id) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> queryDocumentSnapshotList = dbFirestore.collection(collection).get().get().getDocuments();
        List<QueryDocumentSnapshot> queryDocumentSnapshot=queryDocumentSnapshotList.stream().filter(a-> a.getData().get("objectiveId").equals(id)).toList();
        for (QueryDocumentSnapshot queryDocumentSnapshotVar:queryDocumentSnapshot
        ) {
            deleteKeyResult(queryDocumentSnapshotVar.getId());
        }
    }
    public KeyResult setKeyResultFromDocument(DocumentSnapshot documentSnapshot) throws ExecutionException, InterruptedException {
        KeyResult keyResult = new KeyResult();
        if(documentSnapshot.exists()){
            keyResult.setId(documentSnapshot.getId());
            keyResult.setDescription(documentSnapshot.getData().get("description").toString());
            keyResult.setObjectiveId(documentSnapshot.getData().get("objectiveId").toString());
            keyResult.setAction(documentSnapshot.getData().get("action").toString());
            keyResult.setMeasurement(documentSnapshot.getData().get("measurement").toString());
            keyResult.setCheck((Boolean) documentSnapshot.getData().get("check"));
            keyResult.setDay(documentSnapshot.getData().get("day").toString());
            keyResult.setIncreasing((Boolean) documentSnapshot.getData().get("increasing"));
            keyResult.setValue(Integer.parseInt(documentSnapshot.getData().get("value").toString()));
            return keyResult;
        }
        return null;
    }
}
