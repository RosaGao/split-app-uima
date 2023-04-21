package com.example.split;

import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.split.entity.Expense;
import com.example.split.entity.SplitMethod;
import com.example.split.entity.Tag;
import com.example.split.entity.User;
import com.example.split.newExpense.SelectParticipantsActivity;
import com.example.split.newExpense.SelectPayerActivity;
import com.example.split.newExpense.SplitMethodActivity;
import com.example.split.ui.home.HomeFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List<User> finalParticipants = new ArrayList<>();
    public static User finalPayer = null;

    public static Map<User, Double> result = new HashMap<>();

    List<Tag> allTagsFromUserDb = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.newExpenseToolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);

        tag = null;
        method = null;
        finalPayer = null;
        finalParticipants = new ArrayList<>();
        result = new HashMap<>();
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = getIntent().getStringExtra("userId");


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

        mDatabase.child("users").child(userId).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allTagsFromUserDb.clear();
                for (DataSnapshot tagSnapshot : snapshot.getChildren()) {
                    Tag tag = tagSnapshot.getValue(Tag.class);
                    Log.v("tag", tag.getName() + ", num ->" + tag.getNumExpenses());
                    allTagsFromUserDb.add(tag);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        tagChip.setOnClickListener(new View.OnClickListener() {
            String[] tagNames;
            Tag[] tags;

            private void setUp() {
                tagNames = new String[allTagsFromUserDb.size()];
                tags = new Tag[allTagsFromUserDb.size()];
                for (int i = 0; i < allTagsFromUserDb.size(); i++) {
                    tags[i] = allTagsFromUserDb.get(i);
                    tagNames[i] =  tags[i].getName();
                }
            }

            @Override
            public void onClick(View v) {
                setUp();

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
                String amount = editAmount.getText().toString();
                if (TextUtils.isEmpty(amount)) {
                    Snackbar.make(getWindow().getDecorView().getRootView()
                                    , "Must enter a split amount to proceed!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                if (finalParticipants.size() == 0) {
                    Snackbar.make(getWindow().getDecorView().getRootView()
                                    , "Must select participants to proceed!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                if (finalPayer == null) {
                    Snackbar.make(getWindow().getDecorView().getRootView()
                                    , "Must select a payer to proceed!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                Intent splitMethodAct = new Intent(NewExpenseActivity.this, SplitMethodActivity.class);
                splitMethodAct.putExtra("amount", amount);
                startActivity(splitMethodAct);
            }
        });

        SelectParticipantsActivity.fetchUsers();


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (finalParticipants.size() > 0) {
            ImageView img = findViewById(R.id.participantsIcon);
            img.setImageResource(R.drawable.baseline_check_circle_24);
        }

        if (finalPayer != null) {
            ImageView img = findViewById(R.id.payerIcon);
            img.setImageResource(R.drawable.baseline_check_circle_24);
        }

        if (method  == SplitMethod.EQUAL) {
            chooseMethodButton.setImageResource(R.drawable.equalsplit_icon);
        } else if (method == SplitMethod.PERCENT) {
            chooseMethodButton.setImageResource(R.drawable.blackpercentsplit);
        } else if (method == SplitMethod.EXACT) {
            chooseMethodButton.setImageResource(R.drawable.blackexactsplit);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (finalParticipants.size() > 0) {
            ImageView img = findViewById(R.id.participantsIcon);
            img.setImageResource(R.drawable.baseline_check_circle_24);
        }

        if (finalPayer != null) {
            ImageView img = findViewById(R.id.payerIcon);
            img.setImageResource(R.drawable.baseline_check_circle_24);
        }

        if (method  == SplitMethod.EQUAL) {
            chooseMethodButton.setImageResource(R.drawable.equalsplit_icon);
        } else if (method == SplitMethod.PERCENT) {
            chooseMethodButton.setImageResource(R.drawable.blackpercentsplit);
        } else if (method == SplitMethod.EXACT) {
            chooseMethodButton.setImageResource(R.drawable.blackexactsplit);
        }
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

        if (tag == null) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Must choose a tag!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        if (finalParticipants.size() == 0) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Must select participants!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        if (finalPayer == null) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Must select a payer!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        if (method == null) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Must choose a split method!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }

        // increment numExpenses for tag
        String tagId = tag.getTagId();
        mDatabase.child("users").child(userId).child("tags")
                .child(tagId).child("numExpenses").setValue(tag.getNumExpenses() + 1);


        // record expense for book-keeping
        Expense newExpense = new Expense(userId, description, date, amount,
                finalParticipants,
                finalPayer, tag, method, 0.0);
        newExpenseId = mDatabase.child("expenses").push().getKey();
        newExpense.setExpenseId(newExpenseId);
        mDatabase.child("expenses").child(newExpenseId).updateChildren(newExpense.toMap());


        // update relations between non-payers and payer
        DatabaseReference relationsRef =  mDatabase.child("relations");
        relationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (User participant : finalParticipants) {
                    Log.v("participant name", participant.getName());

                    if (participant == finalPayer) {
                        continue;
                    }
                    // if I borrow, then friend is associated with a 'positive' amount
                    // if I am owed, then friend is associated with a 'negative' amount

                    String nonPayerId = participant.getUserId();
                    String payerId = finalPayer.getUserId();

                    Double prevBorrowingsByNonPayer = snapshot.child(nonPayerId).child(payerId).getValue(Double.class);
                    if (prevBorrowingsByNonPayer == null) prevBorrowingsByNonPayer = 0.0;
                    Double prevBorrowingsByPayer = snapshot.child(payerId).child(nonPayerId).getValue(Double.class);
                    if (prevBorrowingsByPayer == null) prevBorrowingsByPayer = 0.0;
                    assert Math.abs(prevBorrowingsByNonPayer) == Math.abs(prevBorrowingsByPayer);

                    Log.v(participant.getName() + " borrow from " + finalPayer.getName(),
                            prevBorrowingsByNonPayer + " -> " + (prevBorrowingsByNonPayer + result.get(participant)));
                    Log.v(finalPayer.getName() + " borrow from " + participant.getName(),
                            prevBorrowingsByPayer + " -> " + (prevBorrowingsByPayer - result.get(participant)));

                    relationsRef.child(nonPayerId).child(payerId).setValue(prevBorrowingsByNonPayer + result.get(participant));
                    relationsRef.child(payerId).child(nonPayerId).setValue(prevBorrowingsByPayer - result.get(participant));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // add expense to expenseList of each participant
        for (User participant : finalParticipants) {
            if (participant == finalPayer || participant.getUserId().equals(finalPayer.getUserId())) {
                continue;
            }

            Log.v("participant name", participant.getName());

            DatabaseReference userRef = mDatabase.child("users").child(participant.getUserId());

            userRef.child("tags").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.v("add tag to participant", "here");
                    Log.v("num children", "" + snapshot.getChildrenCount());

                    Tag participantTag = null;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Tag existingTag = ds.getValue(Tag.class);
                        if (existingTag.getName().equals(tag.getName())) {
                            Log.v("add tag to participant", "found tag!");
                            ds.getRef().child("numExpenses").setValue(existingTag.getNumExpenses() + 1);
                            participantTag = existingTag;
                            break;
                        }
                    }

                    if (participantTag == null) {
                        Log.v("add tag to participant", "add new tag!");
                        String newTagId = userRef.child("tags").push().getKey();
                        Tag newTag = new Tag(tag.getName());
                        newTag.addTaggedExpense();
                        newTag.setTagId(newTagId);
                        userRef.child("tags").child(newTagId).setValue(newTag);
                        participantTag = newTag;
                    }

                    Expense participantExpense = new Expense(participant.getUserId(), description, date, amount,
                            finalParticipants, finalPayer, participantTag, method, 0.0);
                    participantExpense.setExpenseId(newExpenseId);

                    if (participant != finalPayer) {
                        participantExpense.setBorrowing(result.get(participant), false);
                    } else {
                        participantExpense.setBorrowing(Double.parseDouble(amount) - result.get(finalPayer), true);
                    }

                    String key = userRef.child("expenseList").push().getKey();
                    userRef.child("expenseList").child(key).updateChildren(participantExpense.toMap());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
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