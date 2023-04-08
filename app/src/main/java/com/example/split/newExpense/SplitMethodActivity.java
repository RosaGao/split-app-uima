package com.example.split.newExpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.split.R;
import com.google.android.material.chip.Chip;

public class SplitMethodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_method);

        Chip chipEqually = findViewById(R.id.equal_split_method_chip);
        Chip chipPercent = findViewById(R.id.percentage_split_method_chip);
        Chip chipExact = findViewById(R.id.exact_split_method_chip);

        TextView splittingText = findViewById(R.id.splitTextView);

        int yellow = getApplicationContext().getResources().getColor(R.color.yellow);
        int dark_blue = getApplicationContext().getResources().getColor(R.color.dark_blue);
        chipEqually.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipEqually.setChipBackgroundColor(ColorStateList.valueOf(yellow));
                chipEqually.setTextColor(Color.BLACK);
                chipEqually.setChipIconResource(R.drawable.equalsplit_icon);

                chipPercent.setChipBackgroundColor(ColorStateList.valueOf(dark_blue));
                chipPercent.setTextColor(Color.WHITE);
                chipPercent.setChipIconResource(R.drawable.percentsplit_icon);

                chipExact.setChipBackgroundColor(ColorStateList.valueOf(dark_blue));
                chipExact.setTextColor(Color.WHITE);
                chipExact.setChipIconResource(R.drawable.exactsplit_icon);

                splittingText.setText("You are splitting by equal number for (total cost) dollars.");
            }
        });

        chipPercent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipEqually.setChipBackgroundColor(ColorStateList.valueOf(dark_blue));
                chipEqually.setTextColor(Color.WHITE);
                chipEqually.setChipIconResource(R.drawable.whiteequalsplit);

                chipPercent.setChipBackgroundColor(ColorStateList.valueOf(yellow));
                chipPercent.setTextColor(Color.BLACK);
                chipPercent.setChipIconResource(R.drawable.blackpercentsplit);

                chipExact.setChipBackgroundColor(ColorStateList.valueOf(dark_blue));
                chipExact.setTextColor(Color.WHITE);
                chipExact.setChipIconResource(R.drawable.exactsplit_icon);

                splittingText.setText("You are splitting by % for (total cost) dollars.");
            }
        });

        chipExact.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipEqually.setChipBackgroundColor(ColorStateList.valueOf(dark_blue));
                chipEqually.setTextColor(Color.WHITE);
                chipEqually.setChipIconResource(R.drawable.whiteequalsplit);

                chipPercent.setChipBackgroundColor(ColorStateList.valueOf(dark_blue));
                chipPercent.setTextColor(Color.WHITE);
                chipPercent.setChipIconResource(R.drawable.percentsplit_icon);

                chipExact.setChipBackgroundColor(ColorStateList.valueOf(yellow));
                chipExact.setTextColor(Color.BLACK);
                chipExact.setChipIconResource(R.drawable.blackexactsplit);

                splittingText.setText("You are splitting by exact number for (total cost) dollars.");
            }
        });





//        Button btnequally = findViewById(R.id.btnEqually);
//        btnequally.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//
//           }
//        });

        // change content of text view (way of split)
        // participant list ? with adapters . change content in same way as text view, as long as same list of users
    }
}