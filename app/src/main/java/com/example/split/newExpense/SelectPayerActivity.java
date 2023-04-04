package com.example.split.newExpense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.split.R;
import com.example.split.databinding.ActivityMainBinding;
import com.example.split.databinding.ActivityNewExpensePayerBinding;
import com.example.split.entity.User;
import com.google.firebase.database.DatabaseReference;
import com.example.split.newExpense.ParticipantsAdapter;

public class SelectPayerActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private ActivityNewExpensePayerBinding binding;
    public static ParticipantsAdapter adapter;

    public static User payer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense_payer);

        adapter = new ParticipantsAdapter(this, R.layout.activity_new_expense_payer,
                SelectParticipantsActivity.participants);

        binding = ActivityNewExpensePayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}