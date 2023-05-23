package com.example.databasefinalproject.Entity;


public class Blacklist {
    private Integer id;
    private int authorId;
    private int blockedAuthorId;

    // 构造函数、getter和setter方法省略

    public Integer getId() {
        return id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getBlockedAuthorId() {
        return blockedAuthorId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public void setBlockedAuthorId(int blockedAuthorId) {
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
