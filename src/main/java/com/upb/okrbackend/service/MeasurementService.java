package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.upb.okrbackend.models.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class MeasurementService {
    private Firestore dbFirestore;

    public MeasurementService() {
        this.dbFirestore = FirestoreClient.getFirestore();
    }
    public MeasurementService(Firestore dbFirestore){
        this.dbFirestore=dbFirestore;
    }

    public Measurement getMeasurement(String id) throws ExecutionException, InterruptedException {
        dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("Measurement").document(id);
        DocumentSnapshot document = documentReference.get().get();
        Measurement measurement = new Measurement();
        if(document.exists()){
            measurement.setId(document.getId());
            measurement.setActionId(document.getData().get("actionId").toString());
            measurement.setDescription(document.getData().get("description").toString());
            return measurement;
        }
        return null;
    }

    public String createMeasurement(Measurement measurement) throws ExecutionException, InterruptedException {
        dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("Measurement").document(measurement.getId()).set(measurement);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String updateMeasurement(Measurement measurement) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("Measurement").document(measurement.getId()).set(measurement);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String deleteMeasurement(String id) {
        ApiFuture<WriteResult> writeResult = dbFirestore.collection("Measurement").document(id).delete();
        return "Successfully deleted " + id;
    }

    public List<Measurement> getMeasurementListById(DocumentSnapshot documentSnapshot) throws ExecutionException, InterruptedException {
        List<DocumentReference> documentReferenceMeasurement = (List<DocumentReference>) documentSnapshot.getData().get("measurementList");
        Measurement measurementVar;
        List<Measurement> measurementList = new ArrayList<>();
        for (DocumentReference documentReferenceVar : documentReferenceMeasurement
        ) {
            DocumentSnapshot  measurement = dbFirestore.collection("Measurement").document(documentReferenceVar.getId()).get().get();
            measurementVar = new Measurement();
            measurementVar.setId(measurement.getId());
            measurementVar.setDescription(Objects.requireNonNull(documentSnapshot.getData()).get("description").toString());
            measurementVar.setActionId(documentSnapshot.getId());
            measurementList.add(measurementVar);
        }
        return measurementList;
    }
}
