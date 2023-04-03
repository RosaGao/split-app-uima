package com.example.split.entity;

public class Tag {
    private String userId;
    private String name;
    private int numExpenses;

    public Tag(String id, String name) {
        this.userId = id;
        this.name = name;
        this.numExpenses = 0;
    }

    public void addTaggedExpense() {
        this.numExpenses++;
    }

    public int getNumExpenses() {
        return numExpenses;
    }

    public String getName() {
        return name;
    }
}
