package com.example.databasefinalproject.Entity;


public class SecondaryReply {

    private Integer secondaryReplyId;

    private String content;

    private Integer stars;

    private Author author;

    private String authorId;

    private int replyId;

    private Reply reply;

    // Getters and setters

    public Integer getSecondaryReplyId() {
        return secondaryReplyId;
    }

    public String getContent() {
        return content;
    }

    public Integer getStars() {
        return stars;
    }

    public Author getAuthor() {
        return author;
    }

    public Reply getReply() {
        return reply;
    }

    public int getReplyId() {
        return replyId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setSecondaryReplyId(Integer secondaryReplyId) {
        this.secondaryReplyId = secondaryReplyId;
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

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}

