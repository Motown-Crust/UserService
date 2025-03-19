package com.motowncrust.userservice.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    // Fetch user details from Firebase
    public UserRecord getUserByUid(String uid) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().getUser(uid);
    }

    // Create a new Firebase user
    public String createUser(UserRecord.CreateRequest request) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        return userRecord.getUid();
    }

    // Delete a Firebase user
    public void deleteUser(String uid) throws FirebaseAuthException {
        FirebaseAuth.getInstance().deleteUser(uid);
    }

    public UserRecord verifyFirebaseToken(String idToken) throws FirebaseAuthException {
        // Verify the ID token
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = decodedToken.getUid();

        // Get user record
        return FirebaseAuth.getInstance().getUser(uid);
    }
}