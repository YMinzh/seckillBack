package com.example.redis.entity;

public class Order {
    private long id;
    private long proId;
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProId() {
        return proId;
    }

    public void setProId(long proId) {
        this.proId = proId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long users) {
        this.userId = users;
    }
}
