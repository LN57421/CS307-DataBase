package com.example.databasefinalproject.Entity;


import java.sql.Timestamp;

public class Post {

    private Integer postId;

    private String authorId;

    private Author author;

    private String title;

    private String content;

    private Timestamp postingTime;

    private String postingCity;

    private City city;

    // Getters and setters

    public Integer getPostId() {
        return postId;
    }

    public Author getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getPostingTime() {
        return postingTime;
    }

    public City getCity() {
        return city;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getPostingCity() {
        return postingCity;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPostingTime(Timestamp postingTime) {
        this.postingTime = postingTime;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setPostingCity(String postingCity) {
        this.postingCity = postingCity;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", authorId='" + authorId + '\'' +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", postingTime=" + postingTime +
                ", postingCity='" + postingCity + '\'' +
                ", city=" + city +
                '}';
    }
}

