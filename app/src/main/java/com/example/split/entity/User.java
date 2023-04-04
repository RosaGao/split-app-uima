package com.example.split.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class User {
    private String name;                        // Display Name
    private String email;                       // Email
    private String password;                    // Plaintext for now
    private String phone;                       // Phone Number

    private String userID;                      // Uid generated by Firebase after instantiation
    private Map<String, Integer> friends;         // User, amount owed (should be equal magnitude between friends)
    private Set<String> pending_incoming;         // Incoming friend requests
    private Set<String> pending_outgoing;         // Outgoing friend requests
    private List<Expense> expense_list;         // List of expenses

    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.friends = new HashMap<String, Integer>();
        this.pending_incoming = new HashSet<String>();
        this.pending_outgoing = new HashSet<String>();
        this.expense_list = new ArrayList<>();
    }

    public String get_name() { return this.name; }
    public String get_email() { return this.email; }
    public String get_phone() { return this.phone; }
    public void edit_name(String name) { this.name = name; }
    public void edit_email(String email) { this.email = email; }
    public void edit_phone(String phone) { this.phone = phone; }
    public void add_friend_request(String user) {
        this.pending_incoming.add(user);
    }
    public void send_friend_request(String user) {
        this.pending_outgoing.add(user);
    }
    public void accept_friend_request(String user) {
        if(this.pending_incoming.contains(user)) {
            this.pending_incoming.remove(user);
            this.friends.put(user, 0);
        }
    }

    public void change_owed(String user, int amount) {
        if(this.friends.containsKey(user)) {
            this.friends.put(user, amount);
        }
    }
    public int get_owed(User user) {
        return this.friends.getOrDefault(user, 0);
    }

    public void add_expense(Expense expense) { expense_list.add(expense); }
    public void remove_expense (Expense expense) {
        if (expense_list.contains(expense)) {
            expense_list.remove(expense);
        }
    }

    public List<Expense> get_expenses() {
        return this.expense_list;
    }

    public void set_id(String userID) { this.userID = userID; }
    public String get_id() { return this.userID; }

}
