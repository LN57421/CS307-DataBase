package com.example.databasefinalproject.Entity;


public class SharedPost {
    private Integer postId;

    private String sharingAuthorId;

    // getter and setter

    public Integer getPostId() {
        return postId;
    }

    public String getSharingAuthorId() {
        return sharingAuthorId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setSharingAuthorId(String sharingAuthorId) {
        this.sharingAuthorId = sharingAuthorId;
    }

    @Override
    public String toString() {
        return "SharedPost{" +
                "postId=" + postId +
                ", sharingAuthorId='" + sharingAuthorId + '\'' +
                '}';
    }
}
