package com.motowncrust.userservice.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.motowncrust.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get user details by UID
    @GetMapping("/{uid}")
    public ResponseEntity<UserRecord> getUserByUid(@PathVariable String uid) {
        try {
            UserRecord userRecord = userService.getUserByUid(uid);
            System.out.println(userRecord);
            return ResponseEntity.ok(userRecord);
        } catch (FirebaseAuthException e) {
            System.out.println(e);
            return ResponseEntity.status(500).build();
        }
    }

    // Create a new user
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserRecord.CreateRequest request) {
        try {
            String uid = userService.createUser(request);
            System.out.println("User Created:" + uid);
            return ResponseEntity.ok(uid);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
        }
    }

    // Delete a user by UID
    @DeleteMapping("/{uid}")
    public ResponseEntity<String> deleteUser(@PathVariable String uid) {
        try {
            userService.deleteUser(uid);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(500).body("Error deleting user: " + e.getMessage());
        }
    }
}