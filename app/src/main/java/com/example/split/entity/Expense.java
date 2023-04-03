package com.example.split.entity;

import com.example.split.entity.Tag;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Expense {

    String userId;
    private String description;
    private Date date;
    private float amount;
    private Set<User> participants;
    private User payer;
    private Tag tag;
    private SplitMethod method;

    public Expense(String id) {
        this.userId = id;
        this.participants = new HashSet<>();
    }

    public void setDescription(String des) {
        this.description = des;
    }

    public void setDate(Date d) {
        this.date = d;
    }

    public void setAmount(float amt) {
        this.amount = amt;
    }

    public void addParticipant(User user) {
        this.participants.add(user);
    }

    public void setPayer(User user) {
        this.payer = user;
    }

    public void setSplitMethod(SplitMethod m) {
        this.method = m;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public User getPayer() {
        return payer;
    }

    public Tag getTag() {
        return tag;
    }

    public SplitMethod getMethod() {
        return method;
    }

    public float getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return this.userId;
    }
}
