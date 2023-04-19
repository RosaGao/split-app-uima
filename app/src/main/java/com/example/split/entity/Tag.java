package com.example.split.entity;

public class Tag {
    private String name;
    private int numExpenses;

    private String tagId;

    public Tag() {}

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

    public void setName(String newName) { name = newName; }

    public void setTagId(String id) {
        tagId = id;
    }

    public String getTagId() {
        return tagId;
    }
}
