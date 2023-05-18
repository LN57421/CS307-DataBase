package com.example.databasefinalproject.Entity;


public class Reply {

    private Integer replyId;


    private String content;

    private Integer stars;

    private Author author;

    private Post post;

    // Getters and setters

    public Integer getReplyId() {
        return replyId;
    }

    public String getContent() {
        return content;
    }

    public Author getAuthor() {
        return author;
    }

    public Integer getStars() {
        return stars;
    }

    public Post getPost() {
        return post;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
