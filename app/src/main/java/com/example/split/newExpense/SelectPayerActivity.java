package com.example.split.newExpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;


import com.example.split.NewExpenseActivity;
import com.example.split.R;
import com.example.split.databinding.ActivityNewExpenseParticipantsBinding;
import com.example.split.databinding.ActivityNewExpensePayerBinding;
import com.example.split.entity.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

public class SelectPayerActivity extends AppCompatActivity {
    private ActivityNewExpensePayerBinding binding;
    public ParticipantsAdapter adapter;

//    public static User payer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityNewExpensePayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.newExpenseToolbar);
        setSupportActionBar(binding.newExpenseToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewExpenseActivity.finalPayer = null;
                finish();
            }
        });

        adapter = new ParticipantsAdapter(this, R.layout.activity_new_expense_select_payer_layout, NewExpenseActivity.finalParticipants);
        binding.participantlist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_image_button, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.check_image_button) {
            if (NewExpenseActivity.finalPayer == null) {
                Snackbar.make(getWindow().getDecorView().getRootView()
                                , "Must select a payer!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
            for (User participant: NewExpenseActivity.finalParticipants) {
                Log.v("participant:", participant.getName());
            }
            Log.v("payer", NewExpenseActivity.finalPayer.getName());
            SelectParticipantsActivity.instance.finish(); // finish calling activity as well, i.e. pop two activities from stack
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}