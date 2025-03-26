package com.motowncrust.userservice.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.motowncrust.userservice.model.PhoneNumberObject;
import com.motowncrust.userservice.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    private final AdminService adminService;
    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    public AdminUserController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<UserRecord>> getAllUsers() {
        logger.info("Received request to fetch all users.");
        try {
            List<UserRecord> users = adminService.getAllUsers();
            logger.debug("Successfully retrieved {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (FirebaseAuthException e) {
            logger.error("Error fetching users from Firebase", e);
            return ResponseEntity.status(500).build();
        }
    }

    // Get user analytics/activity log
    @GetMapping("/analytics/{uid}")
    public ResponseEntity<List<String>> getUserAnalytics(@PathVariable String uid) {
        logger.info("Received request to fetch analytics for user UID: {}", uid);
        try {
            List<String> analytics = adminService.getUserActivityLog(uid);
            logger.debug("Successfully retrieved analytics for UID: {}", uid);
            return ResponseEntity.ok(analytics);
        } catch (ExecutionException e) {
            logger.error("ExecutionException while fetching user analytics for UID: {}", uid, e);
            return ResponseEntity.status(500).build();
        } catch (InterruptedException e) {
            logger.error("Request interrupted while fetching user analytics for UID: {}", uid, e);
            Thread.currentThread().interrupt();  // Restore interrupt flag
            return ResponseEntity.status(500).build();
        }
    }

    // Get all phone numbers for twillio or other objects
    @GetMapping("/get-phoneNumbers")
    public ResponseEntity<List<PhoneNumberObject>> getNumbers(){
        logger.info("User requested phone numbers at timeStamp: ", new Date().getTime());
        List<PhoneNumberObject> phoneNumberObjects = new ArrayList<>();
        try {
            phoneNumberObjects = adminService.getPhoneNumbers();
            logger.info("Responded admin with phone numbers: ", phoneNumberObjects.size());
            return ResponseEntity.ok(phoneNumberObjects);
        } catch (FirebaseAuthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("Error reciving phone numbers: ", e);
            return ResponseEntity.status(500).build();
        }
    }
}
