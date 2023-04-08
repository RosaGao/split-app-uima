package com.example.split.newExpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;


import com.example.split.NewExpenseActivity;
import com.example.split.R;
import com.example.split.databinding.ActivityMainBinding;
import com.example.split.databinding.ActivityNewExpenseBinding;
import com.example.split.databinding.ActivityNewExpenseParticipantsBinding;
import com.example.split.databinding.ActivityNewExpensePayerBinding;
import com.example.split.entity.User;
import com.google.firebase.database.DatabaseReference;
import com.example.split.newExpense.ParticipantsAdapter;

import java.util.ArrayList;

public class SelectPayerActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private ActivityNewExpensePayerBinding binding;
    public static ParticipantsAdapter adapter;

    public static User payer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        binding = ActivityNewExpensePayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.newExpenseToolbar);
        setSupportActionBar(toolBar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_image_button, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.check_image_button) {
            Intent goToSelectPayer = new Intent(SelectPayerActivity.this, NewExpenseActivity.class);
            startActivity(goToSelectPayer);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}