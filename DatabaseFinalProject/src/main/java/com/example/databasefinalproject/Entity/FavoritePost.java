package com.example.databasefinalproject.Entity;



public class FavoritePost {

    private Integer postId;

    private String favoriteAuthorId;

    // getter and setter


    public Integer getPostId() {
        return postId;
    }

    public String getFavoriteAuthorId() {
        return favoriteAuthorId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setFavoriteAuthorId(String favoriteAuthorId) {
        this.favoriteAuthorId = favoriteAuthorId;
    }


}
