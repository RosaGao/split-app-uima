package com.example.split.entity;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;                        // Display Name
    private String email;                       // Email
    private String password;                    // Plaintext for now
    private int phone;                       // Phone Number

    private Map<User, Integer> friends;         // User, amount owed (should be equal magnitude between friends)
    private User[] pending_incoming;            // Incoming friend requests
    private User[] pending_outgoing;            // Outgoing friend requests

    public User(String name, String email, int phone, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.friends = new HashMap<User, Integer>();
    }

    public String get_name() { return this.name; }
    public String get_email() { return this.email; }
    public int get_phone() { return this.phone; }
    public void edit_name(String name) { this.name = name; }
    public void edit_email(String email) { this.email = email; }
    public void edit_phone(int phone) { this.phone = phone; }
    public void friend_request (User user) {

    }
    // update friends list
    // send friend requests
}
