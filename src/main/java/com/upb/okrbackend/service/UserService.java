package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firestore.v1.Write;
import com.upb.okrbackend.entities.ObjectiveEntity;
import com.upb.okrbackend.entities.UserEntity;
import com.upb.okrbackend.models.Objective;
import com.upb.okrbackend.models.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private Firestore dbFirestore;
    private ObjectiveService objectiveService;
    private String collection = "User";
    public UserService(){
        dbFirestore = FirestoreClient.getFirestore();
        this.objectiveService = new ObjectiveService(dbFirestore);
    }
    public UserService(Firestore dbFirestore){
        this.dbFirestore = dbFirestore;
        this.objectiveService = new ObjectiveService(this.dbFirestore);
    }
    public String createUser(User user) throws ExecutionException, InterruptedException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        user.setCreationdate(com.google.cloud.Timestamp.of(timestamp));
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(user.getId()).set(user);
        return collectionApiFuture.get().getUpdateTime().toString();
    }
    public User getUser(String id) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection(collection).document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        User user = new User();
        if(document.exists()){
            user.setId(document.getId());
            user.setEmail(document.getData().get("email").toString());
            user.setPassword(document.getData().get("password").toString());
            user.setCreationdate(document.getCreateTime());
            user.setName(document.getData().get("name").toString());
            user.setObjectiveList(objectiveService.getObjectiveListById(document));
            return user;
        }
        return null;
    }
    public String updateUser(User user) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = dbFirestore.collection(collection).document(user.getId());
        UserEntity userEntity = new UserEntity();
        userEntity.setCreationdate(com.google.cloud.Timestamp
                .parseTimestamp(Objects.requireNonNull(documentReference.get().get().getData()).get("creationdate").toString()));
        userEntity.setEmail(user.getEmail());
        userEntity.setId(user.getId());
        userEntity.setName(user.getName());
        userEntity.setPassword(user.getPassword());
        List<DocumentReference> documentReferenceList = new ArrayList<>();
        String collectionName = "Objective";
        for (Objective objective: user.getObjectiveList()
             ) {
            String documentName = objective.getId();
            documentReferenceList.add(dbFirestore.collection(collectionName).document(documentName));
        }
        userEntity.setObjectiveList(documentReferenceList);
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(userEntity.getId()).set(userEntity);
        return collectionApiFuture.get().getUpdateTime().toString();
    }
    public String deleteUser(String id){
        ApiFuture<WriteResult> writeResult = dbFirestore.collection(collection).document(id).delete();
        return "Successfully deleted " + id;
    }


}
