package com.example.databasefinalproject.Entity;


public class PostCategory {
    private Integer postId;

    private Integer categoryId;
    // getter and setter

    public Integer getPostId() {
        return postId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public String toString() {
        return "PostCategory{" +
                "postId=" + postId +
                ", categoryId=" + categoryId +
                '}';
    }
}






