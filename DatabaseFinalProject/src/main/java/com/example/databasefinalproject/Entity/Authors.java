package com.example.databasefinalproject.Entity;

import java.sql.Timestamp;

public class Authors {
    private String authorId;
    private String authorName;
    private Timestamp registrationTime;
    private Long phone;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Timestamp getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Authors{" +
                "authorId='" + authorId + '\'' +
                ", authorName='" + authorName + '\'' +
                ", registrationTime=" + registrationTime +
                ", phone=" + phone +
                '}';
    }
}
