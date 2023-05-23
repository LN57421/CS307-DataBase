package com.example.databasefinalproject.Entity;


public class LikedPost {

    private Integer postId;

    private String likingAuthorId;

    // getter and setter


    public Integer getPostId() {
        return postId;
    }

    public String getLikingAuthorId() {
        return likingAuthorId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setLikingAuthorId(String likingAuthorId) {
        this.likingAuthorId = likingAuthorId;
    }

    @Override
    public String toString() {
        return "LikedPost{" +
                "postId=" + postId +
                ", likingAuthorId='" + likingAuthorId + '\'' +
                '}';
    }
}
