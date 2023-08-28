package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.upb.okrbackend.models.Check;
import com.upb.okrbackend.models.KeyValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class KeyValueService {
    private Firestore dbFirestore;
    private String collection="KeyValue";
    public KeyValueService(){
        this.dbFirestore = FirestoreClient.getFirestore();
    }
    public KeyValueService(Firestore dbFirestore){
        this.dbFirestore=dbFirestore;
    }
    public void saveValueByDay(String objectiveId, String day, long value, String keyId) throws ExecutionException, InterruptedException {
        KeyValue keyValue=setKeyValue(objectiveId,day,value,keyId);
        createKeyValue(keyValue);
    }
    public String createKeyValue(KeyValue keyValue) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> collectionApiFuture = dbFirestore.collection(collection).add(keyValue);
        keyValue.setId(collectionApiFuture.get().getId());
        updateKeyValue(keyValue);
        //ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(keyResult.getId()).set(keyResult);
        return Objects.requireNonNull(collectionApiFuture.get().get().get().getUpdateTime()).toString();

    }

    public String updateKeyValue(KeyValue keyValue) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(keyValue.getId()).set(keyValue);
        return collectionApiFuture.get().getUpdateTime().toString();
    }
    public KeyValue getKeyValue(String id) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection(collection).document(id);
        DocumentSnapshot document = documentReference.get().get();
        return setKeyValueFromDocument(document);
    }
    public String deleteKeyValue(String id){
        ApiFuture<WriteResult> writeResult = dbFirestore.collection(collection).document(id).delete();
        return "Successfully deleted " + id;
    }
    public KeyValue setKeyValue( String objectiveId, String day, long value, String keyId){
        KeyValue keyValue = new KeyValue();
        keyValue.setObjectiveId(objectiveId);
        keyValue.setDay(day);
        keyValue.setValue(value);
        keyValue.setKeyId(keyId);
        return keyValue;
    }
    public KeyValue setKeyValueFromDocument(DocumentSnapshot documentSnapshot){
        KeyValue keyValue = new KeyValue();
        if(documentSnapshot.exists()){
            keyValue.setId(documentSnapshot.getId());
            keyValue.setObjectiveId(Objects.requireNonNull(documentSnapshot.getData()).get("objectiveId").toString());
            keyValue.setKeyId(documentSnapshot.getData().get("keyId").toString());
            keyValue.setDay(documentSnapshot.getData().get("day").toString());
            keyValue.setValue(Integer.parseInt(documentSnapshot.getData().get("value").toString()));
            return keyValue;
        }
        return null;
    }
    public List<KeyValue> getAllKeyValueByKeyId(String keyId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> queryDocumentSnapshotList = dbFirestore.collection(collection).get().get().getDocuments();
        if(queryDocumentSnapshotList.size()>0) {
            List<QueryDocumentSnapshot> queryDocumentSnapshot = queryDocumentSnapshotList.stream().filter(a -> a.getData().get("keyId").equals(keyId)).toList();
            List<KeyValue> keyValueList = new ArrayList<>();
            for (QueryDocumentSnapshot queryDocumentSnapshotVar : queryDocumentSnapshot
            ) {
                keyValueList.add(setKeyValueFromDocument(queryDocumentSnapshotVar));
            }
            return keyValueList;
        }
        return new ArrayList<>();
    }
    public List<KeyValue> getAllKeyValueByObjectiveId(String objectiveId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> queryDocumentSnapshotList = dbFirestore.collection(collection).get().get().getDocuments();
        if(queryDocumentSnapshotList.size()>0){


        List<QueryDocumentSnapshot> queryDocumentSnapshot=queryDocumentSnapshotList.stream().filter(a-> a.getData().get("objectiveId").equals(objectiveId)).toList();
        List<KeyValue> keyValueList = new ArrayList<>();
        for (QueryDocumentSnapshot queryDocumentSnapshotVar:queryDocumentSnapshot
        ) {
            keyValueList.add(setKeyValueFromDocument(queryDocumentSnapshotVar));
        }
        return keyValueList;
        }
        return new ArrayList<>();
    }

    public void deleteAllKeyValueFromObjective(String id) throws ExecutionException, InterruptedException {
        List<KeyValue> keyValueList=getAllKeyValueByObjectiveId(id);
        for (KeyValue keyValueVar:keyValueList
             ) {
            deleteKeyValue(keyValueVar.getId());
        }
    }
}
