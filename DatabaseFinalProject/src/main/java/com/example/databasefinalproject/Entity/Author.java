package com.example.databasefinalproject.Entity;

import java.sql.Timestamp;

public class Author {
    private String authorId;
    private String authorName;

    private String authorKey;
    private Timestamp registrationTime;
    private String phone;

    public Author(String authorId, String authorName, String authorKey, Timestamp registrationTime, String phone) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.registrationTime = registrationTime;
        this.phone = phone;
        this.authorKey = authorKey;
    }

    public Author() {
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
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

    public String getAuthorKey() {
        return authorKey;
    }

    public void setAuthorKey(String authorKey) {
        this.authorKey = authorKey;
    }
}
