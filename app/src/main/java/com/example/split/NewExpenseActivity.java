package com.example.split;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.split.entity.Expense;
import com.example.split.entity.SplitMethod;
import com.example.split.entity.Tag;
import com.example.split.entity.User;
import com.example.split.newExpense.SelectParticipantsActivity;
import com.example.split.newExpense.SelectPayerActivity;
import com.example.split.ui.home.HomeFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class NewExpenseActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String userId;
    private String newExpenseId;
    private static final String REQUIRED = "Required";

    EditText editDescription;
    TextView editDate;
    EditText editAmount;

    ImageButton addParticipantsButton;
    Chip tagChip;
    ImageButton chooseMethodButton;

    public static Tag tag = null;
    public static SplitMethod method = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        setSupportActionBar(findViewById(R.id.newExpenseToolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = getIntent().getStringExtra("userId");

        tag = null;

        if (userId == null) {
            Log.v("userId", "null user id");
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Unexpected auth error! Please try again", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            return;
        }

        editDescription = findViewById(R.id.editDescription);
        editDate = findViewById(R.id.editTextDate);
        editAmount = findViewById(R.id.editAmount);

        addParticipantsButton = (ImageButton) findViewById(R.id.addParticipantsButton);
        tagChip = findViewById(R.id.chooseTagButton).findViewById(R.id.tag_layout);
        chooseMethodButton = findViewById(R.id.chooseMethodButton);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        NewExpenseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                editDate.setText( (monthOfYear + 1)  + "-" + dayOfMonth + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        addParticipantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newExpenseToAddParticipants = new Intent(NewExpenseActivity.this, SelectParticipantsActivity.class);
                newExpenseToAddParticipants.putExtra("userId", userId);
                startActivity(newExpenseToAddParticipants);
            }
        });


        if (tag == null) {
            Log.v("tag", "null tag");
            tagChip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
            tagChip.setText("");
        }

        tagChip.setOnClickListener(new View.OnClickListener() {
            final Tag[] tags = new Tag[]{new Tag("id1", "tag 1"),
                    new Tag("id2", "tag 2"), new Tag("id3", "tag 3"),
                    new Tag("id2", "tag 4"), new Tag("id2", "tag 5")};

            final String[] tagNames = new String[]{"tag 1", "tag 2", "tag 3", "tag 4", "tag 5"};

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewExpenseActivity.this);
                alertDialog.setIcon(R.drawable.tags);
                alertDialog.setTitle("Tags");
                alertDialog.setItems(tagNames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tag = tags[which];
                        tagChip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.tag_orange)));
                        tagChip.setText(tag.getName());
                    }
                });

                // set the negative button if the user is not interested to select or change already selected item
                alertDialog.setNegativeButton("Cancel", (dialog, which) -> {
                });

                // create and build the AlertDialog instance with the AlertDialog builder instance
                AlertDialog customAlertDialog = alertDialog.create();
                customAlertDialog.show();
            }
        });

        chooseMethodButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //
            }
        });
    }


    private boolean createNewExpense() {
        String description = editDescription.getText().toString();
        String date = editDate.getText().toString();
        String amount = editAmount.getText().toString();

        if (TextUtils.isEmpty(description)) {
            editDescription.setError(REQUIRED);
            return false;
        }

        if (TextUtils.isEmpty(date)) {
            editDate.setError(REQUIRED);
            return false;
        }

        if (TextUtils.isEmpty(amount)) {
            editDate.setError(REQUIRED);
            return false;
        }

        tag = new Tag("tag_id", "some new tag");
        if (tag == null) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Must choose a tag!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        User newUser = new User("new user", "email", "phone", "pass");
        SelectParticipantsActivity.participants.add(new User("new user", "email", "phone", "pass"));
        if (SelectParticipantsActivity.participants.size() == 0) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Must select participants!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        SelectPayerActivity.payer = newUser;
        if (SelectPayerActivity.payer == null) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Must select a payer!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        method = SplitMethod.EQUAL;
        if (method == null) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Must choose a split method!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        Expense newExpense = new Expense(userId, description, date, amount,
                SelectParticipantsActivity.participants,
                SelectPayerActivity.payer, tag, method);


        newExpenseId = mDatabase.child("expenses").push().getKey();
        newExpense.setExpenseId(newExpenseId);
        mDatabase.child("expenses").child(newExpenseId).setValue(newExpense);

        Snackbar.make(getWindow().getDecorView().getRootView(), "New Expense added", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        HomeFragment.allExpenses.add(newExpense);

        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.check_image_button, menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action with ID action_settings was selected
        if (item.getItemId() == R.id.check_image_button && createNewExpense()) {
            Intent sentResultToHome = new Intent(NewExpenseActivity.this, HomeFragment.class);
            setResult(Activity.RESULT_OK, sentResultToHome);
            finish();
            return true;
        }

        return false;
    }
}