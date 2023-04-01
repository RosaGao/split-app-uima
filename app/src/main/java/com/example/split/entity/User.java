package com.example.split.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class User {
    private String name;                        // Display Name
    private String email;                       // Email
    private String password;                    // Plaintext for now
    private int phone;                       // Phone Number

    private Map<User, Integer> friends;         // User, amount owed (should be equal magnitude between friends)
    private Set<User> pending_incoming;            // Incoming friend requests
    private Set<User> pending_outgoing;            // Outgoing friend requests

    public User(String name, String email, int phone, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.friends = new HashMap<User, Integer>();
        this.pending_incoming = new HashSet<User>();
        this.pending_outgoing = new HashSet<User>();
    }

    public String get_name() { return this.name; }
    public String get_email() { return this.email; }
    public int get_phone() { return this.phone; }
    public void edit_name(String name) { this.name = name; }
    public void edit_email(String email) { this.email = email; }
    public void edit_phone(int phone) { this.phone = phone; }
    public void add_friend_request (User user) {
        this.pending_incoming.add(user);
    }
    public void send_friend_request (User user) {
        this.pending_outgoing.add(user);
    }
    public void accept_friend_request (User user) {
        if(this.pending_incoming.contains(user)) {
            this.pending_incoming.remove(user);
            this.friends.put(user, 0);
        }
    }

    public void change_owed (User user, int amount) {
        if(this.friends.containsKey(user)) {
            this.friends.put(user, amount);
        }
    }
    public int get_owed (User user) {
        return this.friends.getOrDefault(user, 0);
    }

    // generate unique token id
}
