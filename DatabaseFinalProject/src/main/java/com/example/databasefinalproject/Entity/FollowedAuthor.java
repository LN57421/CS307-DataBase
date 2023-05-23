package com.example.databasefinalproject.Entity;


public class FollowedAuthor {

    private String authorId;

    private String followerAuthorId;

    // getter and setter

    public String getAuthorId() {
        return authorId;
    }

    public String getFollowerAuthorId() {
        return followerAuthorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setFollowerAuthorId(String followerAuthorId) {
        this.followerAuthorId = followerAuthorId;
    }

    @Override
    public String toString() {
        return "FollowedAuthor{" +
                "authorId='" + authorId + '\'' +
                ", followerAuthorId='" + followerAuthorId + '\'' +
                '}';
    }
}
