package com.example.split.newExpense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.split.R;

public class SplitMethodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_method);

        Button btnequally = findViewById(R.id.btnEqually);
        btnequally.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
        });

        // change content of text view (way of split)
        // participant list ? with adapters . change content in same way as text view, as long as same list of users
    }
}