package com.example.redisDemo.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Product {
    private long id;
    private String name;
    private Date startTime;
    private Date endTime;
    private int num;
    private BigDecimal money;
    private BigDecimal originPrice;
    private int status;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", num=" + num +
                ", money=" + money +
                ", originPrice=" + originPrice +
                ", status=" + status +
                '}';
    }

    public BigDecimal getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(BigDecimal originPrice) {
        this.originPrice = originPrice;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Product(String name, Date startTime, int num, BigDecimal money) {
        this.name = name;
        this.startTime = startTime;
        this.num = num;
        this.money = money;
    }

    public Product() {
    }
}
