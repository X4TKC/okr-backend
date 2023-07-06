package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.upb.okrbackend.models.KeyResult;
import com.upb.okrbackend.models.Objective;
import com.upb.okrbackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class ObjectiveService {
    private Firestore dbFirestore;
    @Autowired
    private KeyResultService keyResultService;

    public ObjectiveService() {
        this.dbFirestore = FirestoreClient.getFirestore();
        this.keyResultService = new KeyResultService(dbFirestore);
    }

    public Objective getObjective(String id) throws ExecutionException, InterruptedException {
        dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("Objective").document(id);
        DocumentSnapshot document = documentReference.get().get();
        Objective objective = new Objective();
        if(document.exists()){
            List<KeyResult> keyResultList = keyResultService.getKeyResultListById(document);
            objective.setId(document.getId());
            objective.setDate((Timestamp) document.getData().get("date"));
            objective.setName(document.getData().get("name").toString());
            objective.setKeyResultList(keyResultList);
            return objective;
        }
        return null;
    }

    public String createObjective(Objective objective) throws ExecutionException, InterruptedException {
        dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("Objective").document(objective.getName()).set(objective);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String updateObjective(Objective objective) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("Objective").document(objective.getName()).set(objective);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String deleteObjective(String id) {
        dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection("Objective").document(id).delete();
        return "Successfully deleted " + id;
    }
}
