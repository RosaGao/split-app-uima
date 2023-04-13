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

    private String userId;
    private String description;
    private String date;
    private String amount;
    private List<User> participants = new ArrayList<>();
    private User payer;
    private Tag tag;
    private SplitMethod method;

    private String expenseId;

    private Double borrowing;

    public Expense() {}

    public Expense(String userId, String description, String date, String amount, List<User> participants, User payer, Tag tag, SplitMethod method, Double borrowing) {
        this.userId = userId;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.tag = tag;
        this.participants.addAll(participants);
        this.payer = payer;
        this.method = method;
        this.borrowing = borrowing;
    }

    public void setExpenseId(String id) {
        expenseId = id;
    }

    public String getExpenseId() {
        return expenseId;
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

    public void setBorrowing(Double amount, boolean isPayer) {
        // if i borrow, borrow 'positive' amount
        // if i am the payer (others borrow), 'negative' amount
        if (isPayer) {
            borrowing = -amount;
        } else {
            borrowing = amount;
        }
    }

    public double getBorrowing () {
        return borrowing;
    }

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
        result.put("borrowing", borrowing);

        return result;
    }
}
