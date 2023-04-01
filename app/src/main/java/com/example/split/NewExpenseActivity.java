package com.example.split;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.split.entity.User;

public class NewExpenseActivity extends AppCompatActivity {

    public static User payer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        ImageButton addParticipantsButton = (ImageButton) findViewById(R.id.addParticipantsButton);
        addParticipantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newExpenseToAddParticipants = new Intent();
                startActivity(newExpenseToAddParticipants);
            }
        });








    }
}