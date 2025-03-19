package com.motowncrust.userservice.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class AdminService {

    public List<UserRecord> getAllUsers() throws FirebaseAuthException {
        List<UserRecord> usersList = new ArrayList<>();
        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);

        for (UserRecord user : page.iterateAll()) {
            usersList.add(user);
        }

        return usersList;
    }

    public List<String> getUserActivityLog(String uid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        List<String> activityLog = new ArrayList<>();

        QuerySnapshot querySnapshot = db.collection("user_activity")
                .whereEqualTo("uid", uid)
                .get()
                .get();

        for (QueryDocumentSnapshot document : querySnapshot) {
            activityLog.add(document.getString("action") + " at " + document.getString("timestamp"));
        }

        return activityLog;
    }

}
