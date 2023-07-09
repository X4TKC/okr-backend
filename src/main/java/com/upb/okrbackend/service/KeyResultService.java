package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.upb.okrbackend.entities.ObjectiveEntity;
import com.upb.okrbackend.entities.UserEntity;
import com.upb.okrbackend.models.Action;
import com.upb.okrbackend.models.KeyResult;
import com.upb.okrbackend.models.Objective;
import com.upb.okrbackend.models.User;
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
        dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(collection).document(id);
        DocumentSnapshot document = documentReference.get().get();
        KeyResult keyResult = new KeyResult();
        if(document.exists()){

            keyResult.setId(document.getId());
            keyResult.setDescription(document.getData().get("description").toString());
            keyResult.setObjectiveId(document.getData().get("objectiveId").toString());
            keyResult.setAction(document.getData().get("action").toString());
            keyResult.setMeasurement(document.getData().get("measurement").toString());
            return keyResult;
        }
        return null;
    }

    public String createKeyResult(KeyResult keyResult) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(keyResult.getId()).set(keyResult);
        addKeyResultToObjective(keyResult.getId(),keyResult.getObjectiveId());
        return collectionApiFuture.get().getUpdateTime().toString();
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
        KeyResult keyResultVar;
        List<KeyResult> keyResultList = new ArrayList<>();
        for (DocumentReference documentReferenceVar : documentReferenceKeyResult
        ) {
            DocumentSnapshot  keyResult = dbFirestore.collection(collection).document(documentReferenceVar.getId()).get().get();
            keyResultVar = new KeyResult();
            keyResultVar.setId(keyResult.getId());
            keyResultVar.setDescription(Objects.requireNonNull(keyResult.getData()).get("description").toString());
            keyResultVar.setObjectiveId(documentSnapshot.getId());
            keyResultVar.setAction(keyResult.getData().get("action").toString());
            keyResultVar.setMeasurement(keyResult.getData().get("measurement").toString());
            keyResultList.add(keyResultVar);
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
}
