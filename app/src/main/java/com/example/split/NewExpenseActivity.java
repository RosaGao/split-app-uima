package com.example.split;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.split.MainActivity;
import com.example.split.R;
import com.example.split.entity.Expense;
import com.example.split.entity.SplitMethod;
import com.example.split.entity.Tag;
import com.example.split.entity.User;
import com.example.split.expenseList.ExpenseListActivity;
import com.example.split.newExpense.SelectParticipantsActivity;
import com.example.split.newExpense.SelectPayerActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewExpenseActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String userId;
    private static final String REQUIRED = "Required";

    EditText editDescription;
    TextView editDate;
    EditText editAmount;

    ImageButton addParticipantsButton;
    ImageButton chooseTagButton;
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

        if (userId == null) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Unexpected auth error! Please try again", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            finish();
        }

        editDescription = findViewById(R.id.editDescription);
        editDate = findViewById(R.id.editTextDate);
        editAmount = findViewById(R.id.editAmount);

        addParticipantsButton = (ImageButton) findViewById(R.id.addParticipantsButton);
        chooseTagButton = findViewById(R.id.chooseTagButton);
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

        chooseTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        chooseMethodButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //
            }
        });
    }


    private void createNewExpense() {
        String description = editDescription.getText().toString();
        String date = editDate.getText().toString();
        String amount = editAmount.getText().toString();

        if (TextUtils.isEmpty(description)) {
            editDescription.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(date)) {
            editDate.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(amount)) {
            editDate.setError(REQUIRED);
            return;
        }

        tag = new Tag("tag_id", "some new tag");
        if (tag == null) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Must choose a tag!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        User newUser = new User("new user", "email", "phone", "pass");
        SelectParticipantsActivity.participants.add(new User("new user", "email", "phone", "pass"));
        if (SelectParticipantsActivity.participants.size() == 0) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Must select participants!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        SelectPayerActivity.payer = newUser;
        if (SelectPayerActivity.payer == null) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Must select a payer!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        method = SplitMethod.EQUAL;
        if (method == null) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Must choose a split method!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        Expense newExpense = new Expense(userId, description, date, amount,
                SelectParticipantsActivity.participants,
                SelectPayerActivity.payer, tag, method);


        String key = mDatabase.child("expenses").push().getKey();
        newExpense.setExpenseId(key);
        mDatabase.child("expenses").child(key).setValue(newExpense);


        Snackbar.make(getWindow().getDecorView().getRootView(), "New Expense added", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
        if (item.getItemId() == R.id.check_image_button) {
            createNewExpense();
        }

        return true;
    }
}