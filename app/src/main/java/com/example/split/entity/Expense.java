package com.example.split.entity;

import com.example.split.entity.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Expense {

    String userId;
    private String description;
    private String date;
    private String amount;
    private List<User> participants = new ArrayList<>();
    private User payer;
    private Tag tag;
    private SplitMethod method;

    private String expenseId;

    public Expense(String userId, String description, String date, String amount, List<User> participants, User payer, Tag tag, SplitMethod method) {
        this.userId = userId;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.tag = tag;
        this.participants.addAll(participants);
        this.payer = payer;
        this.method = method;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }
    public Tag getTag() {
        return tag;
    }

    public SplitMethod getMethod() {
        return method;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return this.userId;
    }

    public List<User> getParticipants() {return this.participants;}

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("userId", userId);
        result.put("description", description);
        result.put("date", date);
        result.put("amount", amount);
        result.put("tag", tag);
        result.put("participants", participants);
        result.put("payer", payer);
        result.put("method", method);
        result.put("expenseId", expenseId);


        return result;
    }
}
