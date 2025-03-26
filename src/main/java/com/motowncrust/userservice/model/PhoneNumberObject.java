package com.motowncrust.userservice.model;

import com.google.cloud.firestore.annotation.DocumentId;

public class PhoneNumberObject{
    @DocumentId
    private String uid;
    private String phoneNumber;
    private String userName;

    public PhoneNumberObject(String id, String phoneNumber, String userName) {
        this.uid = id;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
    }

    public void setUid(String id) {
        this.uid = id;
    }

    public String getUid() {
        return uid;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}