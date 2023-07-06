package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firestore.v1.Write;
import com.upb.okrbackend.models.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private Firestore dbFirestore;
    public UserService(){
        dbFirestore = FirestoreClient.getFirestore();
    }
    public String createUser(User user) throws ExecutionException, InterruptedException {
        dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("Users").document(user.getName()).set(user);
        return collectionApiFuture.get().getUpdateTime().toString();
    }
    public User getUser(String id) throws ExecutionException, InterruptedException {
        dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("User").document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        User user;
        if(document.exists()){
            user = document.toObject(User.class);
            return user;
        }
        return null;
    }
    public String updateUser(User user) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("User").document(user.getName()).set(user);
        return collectionApiFuture.get().getUpdateTime().toString();
    }
    public String deleteUser(String id){
        dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection("User").document(id).delete();
        return "Successfully deleted " + id;
    }
}
