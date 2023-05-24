package com.example.databasefinalproject.Entity;


public class Blacklist {
    private Integer id;
    private String authorId;
    private String blockedAuthorId;

    // 构造函数、getter和setter方法省略

    public Integer getId() {
        return id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getBlockedAuthorId() {
        return blockedAuthorId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setBlockedAuthorId(String blockedAuthorId) {
        this.blockedAuthorId = blockedAuthorId;
    }

    @Override
    public String toString() {
        return "Blacklist{" +
                "id=" + id +
                ", authorId='" + authorId + '\'' +
                ", blockedAuthorId='" + blockedAuthorId + '\'' +
                '}';
    }
}
