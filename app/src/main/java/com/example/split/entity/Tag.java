package com.example.split.entity;

public class Tag {
    private String name;
    private int numExpenses;

    public Tag(String name) {
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
