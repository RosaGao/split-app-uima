package com.example.split.newExpense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.split.R;
import com.example.split.databinding.ActivityMainBinding;
import com.example.split.databinding.ActivityNewExpenseParticipantsBinding;
import com.example.split.entity.User;

import java.util.ArrayList;
import java.util.List;


public class SelectParticipantsActivity extends AppCompatActivity {

    private ActivityNewExpenseParticipantsBinding binding;
    public static ArrayList<User> users = new ArrayList<>();
    public static UsersAdapter adapter;
    public static List<User> participants = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense_participants);

        adapter = new UsersAdapter(this, R.layout.activity_new_expense_participants);
        participants = new ArrayList<>();


        binding = ActivityNewExpenseParticipantsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}