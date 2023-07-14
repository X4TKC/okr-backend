package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.upb.okrbackend.entities.ObjectiveEntity;
import com.upb.okrbackend.entities.UserEntity;
import com.upb.okrbackend.models.KeyResult;
import com.upb.okrbackend.models.Objective;
import com.upb.okrbackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class ObjectiveService {
    private Firestore dbFirestore;
    @Autowired
    private KeyResultService keyResultService;


    private final String collection="Objective";

    public ObjectiveService() {
        this.dbFirestore = FirestoreClient.getFirestore();

        this.keyResultService = new KeyResultService(this.dbFirestore);

    }
    public ObjectiveService(Firestore dbFirestore) {
        this.dbFirestore = dbFirestore;
        this.keyResultService = new KeyResultService(this.dbFirestore);

    }

    public Objective getObjective(String id) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection(collection).document(id);
        DocumentSnapshot document = documentReference.get().get();
        Objective objective = new Objective();
        if(document.exists()){
            List<KeyResult> keyResultList = keyResultService.getKeyResultListById(document);
            objective.setId(document.getId());
            objective.setUserId(document.getData().get("userId").toString());
            objective.setDateStart(Objects.requireNonNull(document.getData()).get("dateStart").toString());
            objective.setDateEnd(document.getData().get("dateEnd").toString());
            objective.setName(document.getData().get("name").toString());
            objective.setKeyResultList(keyResultList);
            return objective;
        }
        return null;
    }

    public String createObjective(ObjectiveEntity objective) throws ExecutionException, InterruptedException {
//        DocumentSnapshot document = dbFirestore.collection(collection).document(objective.getId()).get().get();
//        List<KeyResult> keyResultList= keyResultService.getKeyResultListById(document);
        List<KeyResult> keyResultList = new ArrayList<>();
        Objective objectiveVar = new Objective(objective.getId(),objective.getName(),keyResultList,objective.getDateStart(),objective.getDateEnd(),objective.getUserId());
        ApiFuture<DocumentReference> collectionApiFuture = dbFirestore.collection(collection).add(objectiveVar);
        objective.setId(collectionApiFuture.get().get().get().getId());
        objectiveVar.setId(objective.getId());
        updateObjective(objective);
        addObjectiveToUser(objectiveVar.getId(), objectiveVar.getUserId());
        return Objects.requireNonNull(collectionApiFuture.get().get().get().getUpdateTime()).toString();
    }

    public String updateObjective(ObjectiveEntity objective) throws ExecutionException, InterruptedException {
//        Timestamp startDate=Timestamp.parseTimestamp(objective.getDateStart());
//        Timestamp endDate=Timestamp.parseTimestamp(objective.getDateEnd());
        DocumentReference documentReference = dbFirestore.collection(collection).document(objective.getId());
        DocumentSnapshot document = documentReference.get().get();
        if(document.exists()) {
            List<KeyResult> keyResultList = keyResultService.getKeyResultListById(document);
            Objective objectiveVar = new Objective(objective.getId(), objective.getName(), keyResultList, objective.getDateStart(), objective.getDateEnd(), objective.getUserId());
            ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(objectiveVar.getId()).set(objectiveVar);

        return collectionApiFuture.get().getUpdateTime().toString();
        }
        return null;
    }



    public String deleteObjective(String id) throws ExecutionException, InterruptedException {
        String userId = dbFirestore.collection(collection).document(id).get().get().getData().get("userId").toString();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection(collection).document(id).delete();
        deleteObjectiveFromUser(userId,id);
        return "Successfully deleted " + id;
    }

    public List<Objective> getObjectiveListById(DocumentSnapshot document) throws ExecutionException, InterruptedException {
        List<DocumentReference> documentReferenceObjective = (List<DocumentReference>) document.getData().get("objectiveList");
        Objective objectiveVar;
        List<Objective> objectiveList = new ArrayList<>();
        for (DocumentReference documentReferenceVar : documentReferenceObjective
        ) {
            DocumentSnapshot  documentObjective = dbFirestore.collection(collection).document(documentReferenceVar.getId()).get().get();
            if(documentObjective.exists()){
            objectiveVar = new Objective();
            objectiveVar.setId(documentObjective.getId());
            objectiveVar.setName(Objects.requireNonNull(documentObjective.getData()).get("name").toString());
            objectiveVar.setDateStart(documentObjective.getData().get("dateStart").toString());
            objectiveVar.setDateEnd(documentObjective.getData().get("dateEnd").toString());
            objectiveVar.setUserId(documentObjective.getData().get("userId").toString());
            objectiveVar.setKeyResultList(keyResultService.getKeyResultListById(documentObjective));
            objectiveList.add(objectiveVar);
            }
        }
        return objectiveList;
    }
    public void addObjectiveToUser(String id, String userId) throws ExecutionException, InterruptedException {
        DocumentSnapshot documentSnapshotUser = dbFirestore.collection("User").document(userId).get().get();
        List<DocumentReference> documentReferenceListUser = (List<DocumentReference>) documentSnapshotUser.getData().get("objectiveList");
        documentReferenceListUser.add(dbFirestore.collection(collection).document(id));
        setUserEntity(documentSnapshotUser, documentReferenceListUser);
    }
    public void deleteObjectiveFromUser(String userId, String objectiveId) throws ExecutionException, InterruptedException {
        DocumentSnapshot documentSnapshotUser = dbFirestore.collection("User").document(userId).get().get();
        if(documentSnapshotUser.exists()) {
            List<DocumentReference> objectiveList=(List<DocumentReference>) documentSnapshotUser.getData().get("objectiveList");
            objectiveList.removeIf(a->a.getId().equals(objectiveId));
            setUserEntity(documentSnapshotUser, objectiveList);
        }
    }

    private void setUserEntity(DocumentSnapshot documentSnapshotUser, List<DocumentReference> objectiveList) {
        UserEntity userEntity = new UserEntity();
        userEntity.setObjectiveList(objectiveList);
        userEntity.setId(documentSnapshotUser.getId());
        userEntity.setEmail(documentSnapshotUser.getData().get("email").toString());
        userEntity.setPassword(documentSnapshotUser.getData().get("password").toString());
        userEntity.setCreationdate(documentSnapshotUser.getCreateTime());
        userEntity.setName(documentSnapshotUser.getData().get("name").toString());
        dbFirestore.collection("User").document(userEntity.getId()).set(userEntity);
    }

    public List<Objective> getAllObjectivesFromUser(String userId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> queryDocumentSnapshotList = dbFirestore.collection(collection).get().get().getDocuments();
        List<QueryDocumentSnapshot> queryDocumentSnapshot=queryDocumentSnapshotList.stream().filter(a-> a.getData().get("userId").equals(userId)).toList();
        List<Objective> objectiveList = new ArrayList<>();
        for (QueryDocumentSnapshot queryDocumentSnapshotVar:queryDocumentSnapshot
             ) {
            Objective objectiveVar= new Objective();
            objectiveVar.setId(queryDocumentSnapshotVar.getId());
            objectiveVar.setUserId(queryDocumentSnapshotVar.getData().get("userId").toString());
            objectiveVar.setName(queryDocumentSnapshotVar.getData().get("name").toString());
            objectiveVar.setDateStart(queryDocumentSnapshotVar.getData().get("dateStart").toString());
            objectiveVar.setDateEnd(queryDocumentSnapshotVar.getData().get("dateEnd").toString());
            objectiveVar.setKeyResultList(keyResultService.getKeyResultListById(queryDocumentSnapshotVar));
            objectiveList.add(objectiveVar);
        }
        return objectiveList;
    }
}
