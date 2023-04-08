package com.example.split.newExpense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.split.NewExpenseActivity;
import com.example.split.R;
import com.example.split.databinding.ActivityMainBinding;
import com.example.split.databinding.ActivityNewExpenseParticipantsBinding;
import com.example.split.entity.User;
import com.example.split.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;
import android.view.Menu;
import android.view.MenuItem;


public class SelectParticipantsActivity extends AppCompatActivity {

    private ActivityNewExpenseParticipantsBinding binding;
    public static ArrayList<User> users = new ArrayList<>();
    public static UsersAdapter adapter;
    public static List<User> participants = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewExpenseParticipantsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.newExpenseToolbar);
        setSupportActionBar(toolBar);

        adapter = new UsersAdapter(this, R.layout.activity_new_expense_participants);
        participants = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_image_button, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.check_image_button) {
            Intent goToSelectPayer = new Intent(SelectParticipantsActivity.this, SelectPayerActivity.class);
            startActivity(goToSelectPayer);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}