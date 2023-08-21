package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.upb.okrbackend.entities.ObjectiveEntity;
import com.upb.okrbackend.entities.UserEntity;
import com.upb.okrbackend.models.KeyResult;
import com.upb.okrbackend.models.Objective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class ObjectiveService {

    private Firestore dbFirestore;
    @Autowired
    private KeyResultService keyResultService;
    @Autowired
    private CheckService checkService;

    @Autowired
    private KeyValueService keyValueService;
    private final String collection="Objective";

    public ObjectiveService() {
        this.dbFirestore = FirestoreClient.getFirestore();
        this.keyResultService = new KeyResultService(this.dbFirestore);
        this.checkService = new CheckService(this.dbFirestore);
    }
    public ObjectiveService(Firestore dbFirestore) {
        this.dbFirestore = dbFirestore;
        this.keyResultService = new KeyResultService(this.dbFirestore);
        this.checkService = new CheckService(this.dbFirestore);
    }

    public Objective getObjective(String id) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection(collection).document(id);
        DocumentSnapshot document = documentReference.get().get();
        return setObjectiveFromDocument(document);
    }

    public String createObjective(ObjectiveEntity objective) throws ExecutionException, InterruptedException {
//        DocumentSnapshot document = dbFirestore.collection(collection).document(objective.getId()).get().get();
//        List<KeyResult> keyResultList= keyResultService.getKeyResultListById(document);
        List<KeyResult> keyResultList = new ArrayList<>();
        Objective objectiveVar = new Objective(objective.getId(),objective.getName(),keyResultList,objective.getDateStart(),objective.getDateEnd(),objective.getUserId());
        ApiFuture<DocumentReference> collectionApiFuture = dbFirestore.collection(collection).add(objectiveVar);
        objective.setId(collectionApiFuture.get().get().get().getId());
        objectiveVar.setId(objective.getId());
        objective.setProgressTracker(0);
        objectiveVar.setProgressTracker(objective.getProgressTracker());
        updateObjective(objective);
        addObjectiveToUser(objectiveVar.getId(), objectiveVar.getUserId());
        checkService.generateAllTheChecksBetweenDates(objective.getDateStart(),objective.getDateEnd(),objective.getId());
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
    public String updateObjectiveEntity(ObjectiveEntity objective) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection(collection).document(objective.getId());
        DocumentSnapshot document = documentReference.get().get();
        if(document.exists()) {
            ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(objective.getId()).set(objective);
            return collectionApiFuture.get().getUpdateTime().toString();
        }
        return null;
    }

    public String deleteObjective(String id) throws ExecutionException, InterruptedException {
        String userId = dbFirestore.collection(collection).document(id).get().get().getData().get("userId").toString();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection(collection).document(id).delete();
        deleteObjectiveFromUser(userId,id);
        keyValueService.deleteAllKeyValueFromObjective(id);
        keyResultService.deleteAllKeyResultFromObjective(id);
        checkService.deleteAllCheckFromObjective(id);
        return "Successfully deleted " + id;
    }

    public List<Objective> getObjectiveListById(DocumentSnapshot document) throws ExecutionException, InterruptedException {
        List<DocumentReference> documentReferenceObjective = (List<DocumentReference>) document.getData().get("objectiveList");
        List<Objective> objectiveList = new ArrayList<>();
        for (DocumentReference documentReferenceVar : documentReferenceObjective
        ) {
            DocumentSnapshot  documentObjective = dbFirestore.collection(collection).document(documentReferenceVar.getId()).get().get();
            objectiveList.add(setObjectiveFromDocument(documentObjective));
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
        userEntity.setEmail(Objects.requireNonNull(documentSnapshotUser.getData()).get("email").toString());
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
            objectiveList.add(setObjectiveFromDocument(queryDocumentSnapshotVar));
        }
        return objectiveList;
    }
    public Objective resetAllKeyResults(String id) throws ExecutionException, InterruptedException {
        Objective objective=this.getObjective(id);
        List<KeyResult> keyResultList = objective.getKeyResultList();
        for (KeyResult keyresult: keyResultList
             ) {
            this.keyResultService.unCheckKeyResult(keyresult.getId());
        }
        ;
        objective=this.getObjective(id);
        return objective;
    }
    public boolean areAllTheKeyResultsChecked(String id) throws ExecutionException, InterruptedException {
        Objective objective=this.getObjective(id);
        List<KeyResult> keyResultList = objective.getKeyResultList();
        boolean check = true;
        for (KeyResult keyresult: keyResultList
        ) {
            check= check && keyresult.getCheck();
        }
        return check;
    }
    public void incrementProgressTracker(String id, String day,long value, String keyId) throws ExecutionException, InterruptedException {
        Objective objective=this.getObjective(id);
        checkService.checkDayByObjective(id,day);
        keyValueService.saveValueByDay(id, day, value, keyId);
        objective.setProgressTracker(objective.getProgressTracker()+1);
        ObjectiveEntity objectiveEntity=this.transformNormalToEntity(objective);
        updateObjectiveEntity(objectiveEntity);
    }
    public ObjectiveEntity transformNormalToEntity(Objective objective){
        ObjectiveEntity objectiveEntity = new ObjectiveEntity();
        objectiveEntity.setId(objective.getId());
        objectiveEntity.setName(objective.getName());
        objectiveEntity.setUserId(objective.getUserId());
        objectiveEntity.setDateStart(objective.getDateStart());
        objectiveEntity.setDateEnd(objective.getDateEnd());
        List<DocumentReference> keyResultListDR = new ArrayList<>();
        List<KeyResult> keyResultList = objective.getKeyResultList();
        //objectiveEntity.setType(objective.getType());
        //objectiveEntity.setState(objective.getState());
        objectiveEntity.setProgressTracker(objective.getProgressTracker());
        for (KeyResult keyresult: keyResultList
             ) {
            keyResultListDR.add(dbFirestore.collection(collection).document(keyresult.getId()));
        }
        objectiveEntity.setKeyResultList(keyResultListDR);
        return objectiveEntity;
    }
    public void isObjectiveCompleted(String id) throws ExecutionException, InterruptedException {
        Objective objective=this.getObjective(id);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDate start = LocalDate.parse(objective.getDateStart(),dtf);
        LocalDate end  = LocalDate.parse(objective.getDateEnd(),dtf);
        long diffInDays = start.datesUntil(end).count();
        if(objective.getProgressTracker()>= diffInDays){
            objective.setState("Completed");
        }
        else {
            objective.setState("Not Completed"); // Adjust status accordingly
        }
        updateObjectiveEntity(this.transformNormalToEntity(objective));
    }
    public boolean checkDailyObjective(String id, String day,long value, String keyId) throws ExecutionException, InterruptedException {
        boolean check=this.areAllTheKeyResultsChecked(id);
        if(check){
            this.incrementProgressTracker(id, day, value, keyId);
            this.isObjectiveCompleted(id);
        }
        return check;
    }
    public Objective setObjectiveFromDocument(DocumentSnapshot documentSnapshot) throws ExecutionException, InterruptedException{
        Objective objective = new Objective();
        if(documentSnapshot.exists()){
            objective.setId(documentSnapshot.getId());
            objective.setName(documentSnapshot.getData().get("name").toString());
            objective.setUserId(documentSnapshot.getData().get("userId").toString());
            objective.setType(documentSnapshot.getData().get("type").toString());
            objective.setDateStart(documentSnapshot.getData().get("dateStart").toString());
            objective.setDateEnd(documentSnapshot.getData().get("dateEnd").toString());
            objective.setState(documentSnapshot.getData().get("state").toString());
            objective.setCheckList(checkService.getAllChecksFromObjective(objective.getId()));
            objective.setKeyResultList(keyResultService.getAllKeyResultsFromObjective(objective.getId()));
            objective.setProgressTracker(Integer.parseInt(documentSnapshot.getData().get("progressTracker").toString()));
            objective.setEnable((Boolean) documentSnapshot.getData().get("enable"));
            return objective;
        }
        return null;
    }
}
