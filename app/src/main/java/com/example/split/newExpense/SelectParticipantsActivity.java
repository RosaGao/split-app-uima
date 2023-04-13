package com.example.split.newExpense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.util.Log;

import com.example.split.NewExpenseActivity;
import com.example.split.R;
import com.example.split.databinding.ActivityMainBinding;
import com.example.split.databinding.ActivityNewExpenseParticipantsBinding;
import com.example.split.entity.User;
import com.example.split.ui.home.HomeFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class SelectParticipantsActivity extends AppCompatActivity {

    private ActivityNewExpenseParticipantsBinding binding;
    public static ArrayList<User> users = new ArrayList<>();
    public static UsersAdapter adapter;
    public static List<User> participants;
    public static SelectParticipantsActivity instance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        binding = ActivityNewExpenseParticipantsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        participants = new ArrayList<>();


        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.newExpenseToolbar);
        setSupportActionBar(binding.newExpenseToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participants.clear();
                finish();
            }
        });

        adapter = new UsersAdapter(this, R.layout.activity_new_expense_select_participant_layout);
        binding.userslist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_image_button, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.check_image_button) {
            if (participants.size() == 0) {
                Snackbar.make(getWindow().getDecorView().getRootView()
                                , "Must select at least one participant!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }

            Set<User> set = new LinkedHashSet<>(participants);
            NewExpenseActivity.finalParticipants.clear();
            NewExpenseActivity.finalParticipants.addAll(set);

            Intent goToSelectPayer = new Intent(SelectParticipantsActivity.this, SelectPayerActivity.class);
            startActivity(goToSelectPayer);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static void fetchUsers() {
        Query query = FirebaseDatabase.getInstance().getReference("users");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();            }
        });
    }

}