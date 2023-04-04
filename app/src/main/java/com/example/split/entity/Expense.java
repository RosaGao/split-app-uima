package com.example.split.entity;

import com.example.split.entity.Tag;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Expense {

    String userId;
    private String description;
    private String date;
    private String amount;
    private String[] participants;
    private String payerId;
    private String tagId;
    private SplitMethod method;

    private String expenseId;

    public Expense(String userId, String description, String date, String amount, String[] participants, String payerId, String tagId, SplitMethod method) {
        this.userId = userId;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.tagId = tagId;
        this.participants = participants;
        this.payerId = payerId;
        this.method = method;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }
    public String getTag() {
        return tagId;
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

    public String[] getParticipants() {return this.participants;}

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("userId", userId);
        result.put("description", description);
        result.put("date", date);
        result.put("amount", amount);
        result.put("tagId", tagId);
        result.put("participants", participants);
        result.put("payerId", payerId);
        result.put("method", method);
        result.put("expenseId", expenseId);

        return result;
    }
}
