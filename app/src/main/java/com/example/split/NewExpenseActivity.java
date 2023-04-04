package com.example.split;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.split.MainActivity;
import com.example.split.R;
import com.example.split.entity.Expense;
import com.example.split.entity.SplitMethod;
import com.example.split.entity.User;
import com.example.split.newExpense.SelectParticipantsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewExpenseActivity extends AppCompatActivity {

    public static User payer;


    private DatabaseReference mDatabase;
    private static final String REQUIRED = "Required";

    EditText editDescription;
    EditText editDate;
    EditText editAmount;
    ImageButton addParticipantsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        editDescription = findViewById(R.id.editDescription);
        editDate = findViewById(R.id.editTextDate);
        editAmount = findViewById(R.id.editAmount);

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
                                editDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });




        addParticipantsButton = (ImageButton) findViewById(R.id.addParticipantsButton);
        addParticipantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newExpenseToAddParticipants = new Intent(NewExpenseActivity.this, SelectParticipantsActivity.class);
                startActivity(newExpenseToAddParticipants);
            }
        });


    }


    private void submitExpense() {
        EditText editDescription = findViewById(R.id.editDescription);
        EditText editDate = findViewById(R.id.editTextDate);
        EditText editAmount = findViewById(R.id.editAmount);

        if (TextUtils.isEmpty(editDescription.getText())) {
            editDescription.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(editDate.getText().toString())) {
            editDate.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(editAmount.getText().toString())) {
            editDate.setError(REQUIRED);
            return;
        }

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
        new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get user value
                User user = dataSnapshot.getValue(User.class);

                if (user == null) {
                    // User is null, error out
                    Log.e("new expense activity", "User " + userId + " is unexpectedly null");
                    Toast.makeText(getApplicationContext(),
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Write new post
                    String[] array={"123","234","345","345"};
                    writeNewExpense(userId, editDescription.getText().toString(), editDate.getText().toString(), editAmount.getText().toString(), "payerID", array, "tagId", SplitMethod.EQUAL);
                }

                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("new expense activity", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    private void writeNewExpense(String userId, String description, String date, String amount, String payerId, String[] participantIds, String tagId, SplitMethod method) {
        String key = mDatabase.child("expenses").push().getKey();
        Expense newExpense = new Expense(userId, description, date, amount, participantIds, payerId, tagId, method);
        newExpense.setExpenseId(key);
        Map<String, Object> expenseValues = newExpense.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/expenses/" + key, expenseValues);
//        childUpdates.put("/user/" + userId + "/" + key, postValues);


        mDatabase.updateChildren(childUpdates);
    }
}