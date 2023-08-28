package com.upb.okrbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.upb.okrbackend.OkrExceptionErrors;
import com.upb.okrbackend.entities.UserEntity;
import com.upb.okrbackend.models.Objective;
import com.upb.okrbackend.models.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
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
    public String createUser(User user) throws ExecutionException, InterruptedException, OkrExceptionErrors {
        if(!userExists(user.getEmail())){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            user.setCreationdate(com.google.cloud.Timestamp.of(timestamp));
            user.setObjectiveList(new ArrayList<>());
            ApiFuture<DocumentReference> collectionApiFuture = dbFirestore.collection(collection).add(user);

            user.setId(collectionApiFuture.get().get().get().getId());
            updateUser(user);
//            ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(user.getId()).set(user);
            return Objects.requireNonNull(collectionApiFuture.get().get().get().getUpdateTime()).toString();
        }else {
            throw new OkrExceptionErrors("User already created");
        }
    }
    public User getUser(String id) throws ExecutionException, InterruptedException, ParseException {
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
    public boolean userExists(String email) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> queryDocumentSnapshotList = dbFirestore.collection(collection).get().get().getDocuments();
        QueryDocumentSnapshot queryDocumentSnapshot=queryDocumentSnapshotList.stream().filter(a-> a.getData().get("email").equals(email)).findFirst().orElse(null);
        return queryDocumentSnapshot != null;
    }


    public User getUserByEmail(String email) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> queryDocumentSnapshotList = dbFirestore.collection(collection).get().get().getDocuments();
        QueryDocumentSnapshot queryDocumentSnapshot=queryDocumentSnapshotList.stream().filter(a-> a.getData().get("email").equals(email)).findFirst().orElse(null);
        User user = new User();
        List<Objective> objectiveList = new ArrayList<>();
        if(queryDocumentSnapshot!=null&&queryDocumentSnapshot.exists()){
            user.setId(queryDocumentSnapshot.getId());
            user.setEmail(queryDocumentSnapshot.getData().get("email").toString());
//            user.setPassword(queryDocumentSnapshot.getData().get("password").toString());
            user.setPassword("nope");
            user.setCreationdate(queryDocumentSnapshot.getCreateTime());
            user.setName(queryDocumentSnapshot.getData().get("name").toString());
            user.setObjectiveList(objectiveList);
            return user;
        }
        return null;
    }
}
