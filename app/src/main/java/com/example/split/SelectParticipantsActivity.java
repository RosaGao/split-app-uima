package com.example.split;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.split.databinding.ActivityMainBinding;
import com.example.split.databinding.ActivityNewExpenseParticipantsBinding;
import com.example.split.entity.User;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Set;


public class SelectParticipantsActivity extends AppCompatActivity {

    private ActivityNewExpenseParticipantsBinding binding;
    public static ArrayList<User> users = new ArrayList<>();
    public static UsersAdapter adapter;
    public static List<User> participants;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense_participants);

        adapter = new UsersAdapter(this, R.layout.activity_new_expense_participants, users);
        participants = new ArrayList<>();


        binding = ActivityNewExpenseParticipantsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}