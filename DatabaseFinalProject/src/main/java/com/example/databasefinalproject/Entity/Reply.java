package com.example.databasefinalproject.Entity;


public class Reply {

    private Integer replyId;


    private String content;

    private Integer stars;

    private Author author;

    private String authorId;

    private int postId;

    private Post post;

    private boolean isAnonymous;

    public void setAnonymous(boolean anonymous) {
        this.isAnonymous = anonymous;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }
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

    public int getPostId() {
        return postId;
    }

    public String getAuthorId() {
        return authorId;
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

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }


    @Override
    public String toString() {
        return "Reply{" +
                "replyId=" + replyId +
                ", content='" + content + '\'' +
                ", stars=" + stars +
                ", author=" + author +
                ", authorId='" + authorId + '\'' +
                ", postId=" + postId +
                ", post=" + post +
                ", is_anonymous=" + isAnonymous +
                '}';
    }
}
