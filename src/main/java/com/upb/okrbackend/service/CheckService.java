package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.upb.okrbackend.entities.KeyResultEntity;
import com.upb.okrbackend.entities.ObjectiveEntity;
import com.upb.okrbackend.models.Check;
import com.upb.okrbackend.models.KeyResult;
import com.upb.okrbackend.models.Objective;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class CheckService {
    private Firestore dbFirestore;
    private String collection="Check";

    public CheckService(){
        this.dbFirestore = FirestoreClient.getFirestore();
    }
    public CheckService(Firestore dbFirestore){
        this.dbFirestore = dbFirestore;
    }
    public Check getCheck(String id) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection(collection).document(id);
        DocumentSnapshot document = documentReference.get().get();
        return setCheckFromDocument(document);
    }
    public String deleteCheck(String id){
        ApiFuture<WriteResult> writeResult = dbFirestore.collection(collection).document(id).delete();
        return "Successfully deleted " + id;
    }
    public String updateCheck(Check check) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(check.getId()).set(check);
        return collectionApiFuture.get().getUpdateTime().toString();
    }
    public String createCheck(Check check) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> collectionApiFuture = dbFirestore.collection(collection).add(check);
        check.setId(collectionApiFuture.get().getId());
        updateCheck(check);
        //ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(keyResult.getId()).set(keyResult);
        addCheckToObjective(check.getId(),check.getObjectiveId());
        return Objects.requireNonNull(collectionApiFuture.get().get().get().getUpdateTime()).toString();
    }
    public List<Check> getCheckListById(DocumentSnapshot documentSnapshot) throws ExecutionException, InterruptedException {
        List<DocumentReference> documentReferenceKeyResult = (List<DocumentReference>) documentSnapshot.getData().get("checkList");
        List<Check> checkList = new ArrayList<>();
        for (DocumentReference documentReferenceVar : documentReferenceKeyResult
        ) {
            DocumentSnapshot  documentKeyResult = dbFirestore.collection(collection).document(documentReferenceVar.getId()).get().get();
            checkList.add(setCheckFromDocument(documentKeyResult));
        }
        return checkList;
    }
    public void addCheckToObjective(String id, String objectiveId) throws ExecutionException, InterruptedException {
        DocumentSnapshot documentSnapshotObjective = dbFirestore.collection("Objective").document(objectiveId).get().get();
        List<DocumentReference> documentReferenceListCheck = (List<DocumentReference>) documentSnapshotObjective.getData().get("checkList");
        List<DocumentReference> documentReferenceListKeyResult = (List<DocumentReference>) documentSnapshotObjective.getData().get("keyResultList");
        documentReferenceListCheck.add(dbFirestore.collection(collection).document(id));
        ObjectiveEntity objectiveEntity = new ObjectiveEntity();
        objectiveEntity.setKeyResultList(documentReferenceListKeyResult);
        objectiveEntity.setCheckList(documentReferenceListCheck);
        objectiveEntity.setId(documentSnapshotObjective.getId());
        objectiveEntity.setName(documentSnapshotObjective.getData().get("name").toString());
        objectiveEntity.setProgressTracker(Integer.parseInt(documentSnapshotObjective.getData().get("progressTracker").toString()));
        objectiveEntity.setType(documentSnapshotObjective.getData().get("type").toString());
        objectiveEntity.setState(documentSnapshotObjective.getData().get("state").toString());
        objectiveEntity.setUserId(documentSnapshotObjective.getData().get("userId").toString());
        objectiveEntity.setDateStart(documentSnapshotObjective.getData().get("dateStart").toString());
        objectiveEntity.setDateEnd(documentSnapshotObjective.getData().get("dateEnd").toString());
        objectiveEntity.setEnable((Boolean) documentSnapshotObjective.getData().get("enable"));
        dbFirestore.collection("Objective").document(objectiveEntity.getId()).set(objectiveEntity);
    }
    public List<Check> getAllChecksFromObjective(String objectiveId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> queryDocumentSnapshotList = dbFirestore.collection(collection).get().get().getDocuments();
        List<QueryDocumentSnapshot> queryDocumentSnapshot=queryDocumentSnapshotList.stream().filter(a-> a.getData().get("objectiveId").equals(objectiveId)).toList();
        List<Check> checkList = new ArrayList<>();
        for (QueryDocumentSnapshot queryDocumentSnapshotVar:queryDocumentSnapshot
        ) {
            checkList.add(setCheckFromDocument(queryDocumentSnapshotVar));
        }
        return checkList;
    }
    public List<Check> getAllChecksFromKeyResult(String keyResultId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> queryDocumentSnapshotList = dbFirestore.collection(collection).get().get().getDocuments();
        System.out.println(queryDocumentSnapshotList.size());
        List<QueryDocumentSnapshot> queryDocumentSnapshot=queryDocumentSnapshotList.stream().filter(a-> a.getData().get("keyResultId") != null && a.getData().get("keyResultId").equals(keyResultId)).toList();
        List<Check> checkList = new ArrayList<>();
        for (QueryDocumentSnapshot queryDocumentSnapshotVar:queryDocumentSnapshot
        ) {
            checkList.add(setCheckFromDocument(queryDocumentSnapshotVar));
        }
        return checkList;
    }
    public String checkIndividualCheck(String id) throws ExecutionException, InterruptedException {
        Check check=this.getCheck(id);
        check.setChecked(true);
        return this.updateCheck(check);
    }
    public String unCheckIndividualCheck(String id) throws ExecutionException, InterruptedException {
        Check check=this.getCheck(id);
        check.setChecked(false);
        return this.updateCheck(check);
    }

    public void deleteAllCheckFromObjective(String id) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> queryDocumentSnapshotList = dbFirestore.collection(collection).get().get().getDocuments();
        List<QueryDocumentSnapshot> queryDocumentSnapshot=queryDocumentSnapshotList.stream().filter(a-> a.getData().get("objectiveId").equals(id)).toList();
        for (QueryDocumentSnapshot queryDocumentSnapshotVar:queryDocumentSnapshot
        ) {
            deleteCheck(queryDocumentSnapshotVar.getId());
        }
    }
    public Check setCheckFromDocument(DocumentSnapshot documentSnapshot){
        Check check = new Check();
        if(documentSnapshot.exists()){
            check.setId(documentSnapshot.getId());
            check.setObjectiveId(Objects.requireNonNull(documentSnapshot.getData()).get("objectiveId").toString());
            Object var=documentSnapshot.getData().get("keyResultId");
            check.setKeyResultId(var!=null?documentSnapshot.getData().get("keyResultId").toString():null);
            check.setCheckDate(documentSnapshot.getData().get("checkDate").toString());
            check.setChecked((Boolean) documentSnapshot.getData().get("checked"));
            check.setValue(Long.parseLong(documentSnapshot.getData().get("value").toString()));
            return check;
        }
        return null;
    }
    public List<Check> generateAllTheChecksBetweenDates(String startDate, String endDate, String id) throws ExecutionException, InterruptedException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate,dtf);
        LocalDate end  = LocalDate.parse(endDate,dtf);
        long diffInDays = start.datesUntil(end).count();
        List<LocalDate> listOfDates = new ArrayList<>(start.datesUntil(end)
                .toList());
        listOfDates.add(end);
        List<Check> checkList = new ArrayList<>();
        for (LocalDate localdate: listOfDates
             ) {
            Check check = new Check();
            check.setCheckDate(String.valueOf(localdate));
            check.setObjectiveId(id);
            check.setChecked(false);
            createCheck(check);
            checkList.add(check);
        }
        return checkList;
    }
    public List<Check> generateAllTheChecksBetweenDatesAndAddKeyResultId(String startDate, String endDate, String id,String keyId) throws ExecutionException, InterruptedException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate,dtf);
        LocalDate end  = LocalDate.parse(endDate,dtf);
        List<LocalDate> listOfDates = new ArrayList<>(start.datesUntil(end)
                .toList());
        listOfDates.add(end);
        List<Check> checkList = new ArrayList<>();
        for (LocalDate localdate: listOfDates
        ) {
            Check check = new Check();
            check.setCheckDate(String.valueOf(localdate));
            check.setObjectiveId(id);
            check.setKeyResultId(keyId);
            check.setChecked(false);
            createCheck(check);
            checkList.add(check);
        }
        return checkList;
    }
    public void checkDayByObjective(String keyId, String day, long value) throws ExecutionException, InterruptedException {
        List<Check> checkList=getAllChecksFromKeyResult(keyId);
        String formatDay= day.substring(0,10);
        Check check=checkList.stream().filter(a-> a.getCheckDate().equals(formatDay)).findFirst().orElse(null);
        if (check != null) {
            check.setChecked(true);
            check.setValue(value);
            updateCheck(check);
        }
    }

    public void addKeyResultToCheckList(String keyId, String objId,String dateStart, String dateEnd) throws ExecutionException, InterruptedException {
        List<Check> checkList=getAllChecksFromObjective(objId);
        int counter=0;
        for (Check check: checkList
             ) {
            if(check.getKeyResultId()==null){
                check.setKeyResultId(keyId);
                updateCheck(check);
                counter++;
            }
        }
        if (counter == 0) {
            generateAllTheChecksBetweenDatesAndAddKeyResultId(dateStart,dateEnd,objId,keyId);
        }
    }
}
