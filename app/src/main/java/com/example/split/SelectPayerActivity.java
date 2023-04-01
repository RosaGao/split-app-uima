package com.example.split;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.split.databinding.ActivityMainBinding;
import com.example.split.databinding.ActivityNewExpensePayerBinding;
import com.example.split.entity.User;

public class SelectPayerActivity extends AppCompatActivity {

    private ActivityNewExpensePayerBinding binding;
    public static ParticipantsAdapter adapter;


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