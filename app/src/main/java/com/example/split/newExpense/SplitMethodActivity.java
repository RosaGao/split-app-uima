package com.example.split.newExpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.split.NewExpenseActivity;
import com.example.split.R;

import com.example.split.databinding.ActivitySplitMethodBinding;
import com.example.split.entity.SplitMethod;
import com.example.split.entity.User;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SplitMethodActivity extends AppCompatActivity {

    ActivitySplitMethodBinding binding;
    SplitMethodParticipantAdapter adapter;
    public static SplitMethod method = SplitMethod.EQUAL;
    public static String amount;
    List<User> participantsToSplit = new ArrayList<>(NewExpenseActivity.finalParticipants);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplitMethodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.newExpenseToolbar);
        setSupportActionBar(binding.newExpenseToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewExpenseActivity.method = null; // nullify any selection since didn't hit check button
                finish();
            }
        });

        Chip chipEqually = findViewById(R.id.equal_split_method_chip);
        Chip chipPercent = findViewById(R.id.percentage_split_method_chip);
        Chip chipExact = findViewById(R.id.exact_split_method_chip);

        TextView splittingText = findViewById(R.id.splitTextView);

        int yellow = getApplicationContext().getResources().getColor(R.color.yellow);
        int dark_blue = getApplicationContext().getResources().getColor(R.color.dark_blue);

        amount = getIntent().getStringExtra("amount");
        splittingText.setText("You are splitting by equal number for " + amount + " dollars.");


        chipEqually.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method = SplitMethod.EQUAL;
                adapter.notifyDataSetChanged();
                chipEqually.setChipBackgroundColor(ColorStateList.valueOf(yellow));
                chipEqually.setTextColor(Color.BLACK);
                chipEqually.setChipIconResource(R.drawable.equalsplit_icon);

                chipPercent.setChipBackgroundColor(ColorStateList.valueOf(dark_blue));
                chipPercent.setTextColor(Color.WHITE);
                chipPercent.setChipIconResource(R.drawable.percentsplit_icon);

                chipExact.setChipBackgroundColor(ColorStateList.valueOf(dark_blue));
                chipExact.setTextColor(Color.WHITE);
                chipExact.setChipIconResource(R.drawable.exactsplit_icon);

                splittingText.setText("You are splitting by equal number for " + amount + " dollars.");
            }
        });

        chipPercent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method = SplitMethod.PERCENT;
                adapter.notifyDataSetChanged();

                chipEqually.setChipBackgroundColor(ColorStateList.valueOf(dark_blue));
                chipEqually.setTextColor(Color.WHITE);
                chipEqually.setChipIconResource(R.drawable.whiteequalsplit);

                chipPercent.setChipBackgroundColor(ColorStateList.valueOf(yellow));
                chipPercent.setTextColor(Color.BLACK);
                chipPercent.setChipIconResource(R.drawable.blackpercentsplit);

                chipExact.setChipBackgroundColor(ColorStateList.valueOf(dark_blue));
                chipExact.setTextColor(Color.WHITE);
                chipExact.setChipIconResource(R.drawable.exactsplit_icon);

                splittingText.setText("You are splitting by % for " + amount + " dollars.");
            }
        });

        chipExact.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method = SplitMethod.EXACT;
                adapter.notifyDataSetChanged();

                chipEqually.setChipBackgroundColor(ColorStateList.valueOf(dark_blue));
                chipEqually.setTextColor(Color.WHITE);
                chipEqually.setChipIconResource(R.drawable.whiteequalsplit);

                chipPercent.setChipBackgroundColor(ColorStateList.valueOf(dark_blue));
                chipPercent.setTextColor(Color.WHITE);
                chipPercent.setChipIconResource(R.drawable.percentsplit_icon);

                chipExact.setChipBackgroundColor(ColorStateList.valueOf(yellow));
                chipExact.setTextColor(Color.BLACK);
                chipExact.setChipIconResource(R.drawable.blackexactsplit);

                splittingText.setText("You are splitting by exact number for " + amount + " dollars.");
            }
        });


//        participantsToSplit.remove(NewExpenseActivity.finalPayer);
        adapter = new SplitMethodParticipantAdapter(this, R.layout.activity_split_method_participant_layout, participantsToSplit);
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
            View v;

            Log.v("split method", String.valueOf(method));

            if (method == SplitMethod.EQUAL) {
                int num = participantsToSplit.size();
                double value = Double.parseDouble(amount) / num;
                for (User participant: participantsToSplit) {
                    NewExpenseActivity.result.put(participant, value);
                }
                NewExpenseActivity.method = method;

                for (User participant: participantsToSplit) {
                    Log.v("result", participant.getName() + " " +  NewExpenseActivity.result.get(participant));
                }

                finish();
                return true;
            }

            List<Double> inputs = new ArrayList<>();
            double total = 0;

            EditText edittext;

            for (int i = 0; i < participantsToSplit.size(); i++) {
                User participant = participantsToSplit.get(i);

                v = binding.userslist.getChildAt(i);
                edittext = (EditText) v.findViewById(R.id.inputtextedit);
                String input = edittext.getText().toString();
                if (TextUtils.isEmpty(input)) {
                    edittext.setError("Required");
                    return false;
                }

                try {
                    double value = Double.parseDouble(input);
                    Log.v("input", input);
                    inputs.add(Double.valueOf(input));
                    total += value;
                } catch (Exception e) {
                    Snackbar.make(getWindow().getDecorView().getRootView()
                                    , "Invalid input format!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return false;
                }
            }

            Log.v("total", Double.toString(total));

            if (method == SplitMethod.PERCENT && total != 100) {
                Snackbar.make(getWindow().getDecorView().getRootView()
                                , "Oops! The percents don't add up to 100%. ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }

            if (method == SplitMethod.EXACT && total != Double.parseDouble(amount)) {
                Snackbar.make(getWindow().getDecorView().getRootView()
                                , "Oops! The exact amounts don't add up to " + amount, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }

            double splitAmount = Double.parseDouble(amount);
            int numParticipants = participantsToSplit.size() ;


            for (int i = 0; i < participantsToSplit.size(); i++) {
                User participant = participantsToSplit.get(i);
                Double input = inputs.get(i);

                if (method == SplitMethod.PERCENT) {
                    NewExpenseActivity.result.put(participant, splitAmount * input / 100);
                } else if (method == SplitMethod.EXACT) {
                    NewExpenseActivity.result.put(participant, input);
                }
            }


            for (User participant: participantsToSplit) {
                Log.v("result", participant.getName() + " " +  NewExpenseActivity.result.get(participant));
            }


            NewExpenseActivity.method = method;
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}