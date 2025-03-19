package com.motowncrust.userservice.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.motowncrust.userservice.service.UserService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get user details by UID
    @GetMapping("/{uid}")
    public ResponseEntity<UserRecord> getUserByUid(@PathVariable String uid) {
        logger.info("Fetching user with ID: {}", uid);
        try {
            UserRecord userRecord = userService.getUserByUid(uid);
            logger.debug("User record retrieved {}",userRecord);
            return ResponseEntity.ok(userRecord);
        } catch (FirebaseAuthException e) {
            System.out.println(e);
            logger.error("Firebase error occurred: {}",uid,e);
            return ResponseEntity.status(500).build();
        }
    }

    // Create a new user
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserRecord.CreateRequest request) {
        logger.info("Received request to create a new user with user: {}", request);
        try {
            String uid = userService.createUser(request);
            logger.info("User successfully created with UID: {}", uid);
            return ResponseEntity.ok(uid);
        } catch (FirebaseAuthException e) {
            logger.error("Error creating user with user: {}", request, e);
            return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
        }
    }

    // Delete a user by UID
    @DeleteMapping("/{uid}")
    public ResponseEntity<String> deleteUser(@PathVariable String uid) {
        logger.info("Received request to delete user with UID: {}", uid);
        try {
            userService.deleteUser(uid);
            logger.info("User successfully deleted: {}", uid);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (FirebaseAuthException e) {
            logger.error("Error deleting user with UID: {}", uid, e);
            return ResponseEntity.status(500).body("Error deleting user: " + e.getMessage());
        }
    }

    // Verify Firebase Token
    @PostMapping("/verify-token")
    public ResponseEntity<UserRecord> verifyToken(@RequestParam String idToken) {
        logger.info("Received request to verify Firebase token");
        try {
            UserRecord userRecord = userService.verifyFirebaseToken(idToken);
            logger.info("Token successfully verified for user: {}", userRecord.getUid());
            return ResponseEntity.ok(userRecord);
        } catch (FirebaseAuthException e) {
            logger.error("Error verifying Firebase token", e);
            return ResponseEntity.status(401).body(null);
        }
    }

}