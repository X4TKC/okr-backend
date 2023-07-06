package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
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
    @Autowired
    private ActionService actionService;

    public KeyResultService() {
        this.dbFirestore = FirestoreClient.getFirestore();
        this.actionService = new ActionService(dbFirestore);
    }
    public KeyResultService(Firestore dbFirestore){
        this.dbFirestore=dbFirestore;
        this.actionService = new ActionService(this.dbFirestore);
    }

    public KeyResult getKeyResult(String id) throws ExecutionException, InterruptedException {
        dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("KeyResult").document(id);
        DocumentSnapshot document = documentReference.get().get();
        KeyResult keyResult;
        if(document.exists()){
            keyResult = document.toObject(KeyResult.class);
            return keyResult;
        }
        return null;
    }

    public String createKeyResult(KeyResult keyResult) throws ExecutionException, InterruptedException {
        dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("KeyResult").document(keyResult.getId()).set(keyResult);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String updateKeyResult(KeyResult keyResult) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("KeyResult").document(keyResult.getId()).set(keyResult);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String deleteKeyResult(String id) {
        dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection("KeyResult").document(id).delete();
        return "Successfully deleted " + id;
    }

    public List<KeyResult> getKeyResultListById(DocumentSnapshot documentSnapshot) throws ExecutionException, InterruptedException {
        List<DocumentReference> documentReferenceKeyResult = (List<DocumentReference>) documentSnapshot.getData().get("keyResultList");
        KeyResult keyResultVar;
        List<KeyResult> keyResultList = new ArrayList<>();
        for (DocumentReference documentReferenceVar : documentReferenceKeyResult
        ) {
            DocumentSnapshot  keyResult = dbFirestore.collection("KeyResult").document(documentReferenceVar.getId()).get().get();
            keyResultVar = new KeyResult();
            keyResultVar.setId(keyResult.getId());
            keyResultVar.setDescription(Objects.requireNonNull(keyResult.getData()).get("description").toString());
            keyResultVar.setObjectiveId(documentSnapshot.getId());
            keyResultVar.setActionList(actionService.getActionListById(keyResult));
            keyResultList.add(keyResultVar);
        }
        return keyResultList;
    }
}
