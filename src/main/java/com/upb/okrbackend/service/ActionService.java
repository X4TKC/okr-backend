package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
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
public class ActionService {
    private Firestore dbFirestore;
    @Autowired
    private MeasurementService measurementService;

    public ActionService() {
        this.dbFirestore = FirestoreClient.getFirestore();
        this.measurementService = new MeasurementService(this.dbFirestore);
    }
    public ActionService(Firestore dbFirestore){
        this.dbFirestore=dbFirestore;
        this.measurementService= new MeasurementService(this.dbFirestore);
    }

    public Action getAction(String id) throws ExecutionException, InterruptedException {
        dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("Action").document(id);
        DocumentSnapshot document = documentReference.get().get();
        Action action;
        if(document.exists()){
            action = document.toObject(Action.class);
            return action;
        }
        return null;
    }

    public String createAction(Action action) throws ExecutionException, InterruptedException {
        dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("Action").document(action.getId()).set(action);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String updateAction(Action action) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("Action").document(action.getId()).set(action);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String deleteAction(String id) {
        dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection("Action").document(id).delete();
        return "Successfully deleted " + id;
    }

    public List<Action> getActionListById(DocumentSnapshot documentSnapshot) throws ExecutionException, InterruptedException {
        List<DocumentReference> documentReferenceAction = (List<DocumentReference>) documentSnapshot.getData().get("actionList");
        Action actionVar;
        List<Action> actionList = new ArrayList<>();
        for (DocumentReference documentReferenceVar : documentReferenceAction
        ) {
            DocumentSnapshot  action = dbFirestore.collection("Action").document(documentReferenceVar.getId()).get().get();
            actionVar = new Action();
            actionVar.setId(action.getId());
            actionVar.setDescription(Objects.requireNonNull(action.getData()).get("description").toString());
            actionVar.setKeyId(documentSnapshot.getId());
            actionVar.setMeasurementList(measurementService.getMeasurementListById(action));
            actionList.add(actionVar);
        }
        return actionList;
    }
}
