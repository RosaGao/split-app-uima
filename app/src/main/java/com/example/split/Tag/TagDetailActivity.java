package com.example.split.Tag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.split.NewExpenseActivity;
import com.example.split.R;
import com.example.split.entity.Expense;
import com.example.split.entity.Tag;
import com.example.split.entity.User;
import com.example.split.expenseList.ExpenseListRecyclerViewAdapter;
import com.example.split.ui.home.HomeFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TagDetailActivity extends AppCompatActivity {

    private static final int LAUNCH_NEW_EXPENSE_REQUEST_CODE = 1000;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    DatabaseReference userDataRef;
    private static String userId;
    private static String tagId;
    private static User currentUser = null;
    public static List<Expense> allExpenses = new ArrayList<>();
    public TagExpenseListRecyclerViewAdapter myAdapt;

    public static Tag myTag;
    private TextView tagTitle;
    private TextView tagTotal;
    private List<Expense> expensesWithThisTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);

        Intent intent = getIntent();
        tagId = intent.getStringExtra("tag_id");
        if (tagId == null) {
            finish();
        }
        Log.v("tagid", tagId);

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();

        tagTitle = findViewById(R.id.tagTitleTextView);
        tagTotal = findViewById(R.id.totalCalculationTextView);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.v("userid", userId);
        dbRef.child("users").child(userId).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.v("num", snapshot.getChildrenCount() + "");
                myTag = snapshot.child(tagId).getValue(Tag.class);
                tagTitle.setText(myTag.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (myTag != null) {
            tagTitle.setText(myTag.getName());
            expensesWithThisTag = getExpensesWithTag();
            RecyclerView recyclerView = findViewById(R.id.expense_list_tag);
            myAdapt = new TagExpenseListRecyclerViewAdapter(this, expensesWithThisTag, false);
            recyclerView.setAdapter(myAdapt);

            double total = getTotal(expensesWithThisTag);
            DecimalFormat df = new DecimalFormat("0.00");

            if (total == 0) {
                tagTotal.setText("You are owed $" + df.format(total));
                tagTotal.setTextColor(getResources().getColor(R.color.black));
            } else if (total > 0) {
                tagTotal.setText("You borrowed $" + df.format(total));
                tagTotal.setTextColor(getResources().getColor(R.color.red));
            } else {
                tagTotal.setText("You are owed $" + df.format(Math.abs(total)));
                tagTotal.setTextColor(getResources().getColor(R.color.green));
            }

        }

    }

    private List<Expense> getExpensesWithTag() {
        List<Expense> allMyExpenses = HomeFragment.allExpenses;
        List<Expense> expensesWithMyTag = new ArrayList<>();
        String tagId = myTag.getTagId();

        for (Expense exp : allMyExpenses) {
            if (exp.getTag().getTagId().equals(tagId)) {
                expensesWithMyTag.add(exp);
            }
        }
        return expensesWithMyTag;
    }

    private void saveNewTagNameToDatabase(String tagName) {
        DatabaseReference tagDbRf = dbRef.child("users").child(userId).child("tags").child(tagId);
        tagDbRf.child("name").setValue(tagName);
    }

    private double getTotal(List<Expense> expenses) {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getBorrowing();
        }
        return total;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.two_image_buttons, menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action with ID action_settings was selected
        if (item.getItemId() == R.id.trash_image_button) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TagDetailActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_delete, null);
            builder.setView(dialogView);

            builder.setTitle("Delete tag permanently?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (expensesWithThisTag.size() == 0) {
                                DatabaseReference tagDbRef = dbRef.child("users").child(userId).child("tags").child(tagId);
                                tagDbRef.removeValue();
                                finish();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.edit_image_button) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TagDetailActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_tag, null);
            builder.setView(dialogView);

            final EditText tagNameInput = dialogView.findViewById(R.id.tag_name_input);

            builder.setTitle("Edit tag name")
                    .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String tagName = tagNameInput.getText().toString();
                            if (!tagName.isEmpty()) {
                                saveNewTagNameToDatabase(tagName);
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        return false;
    }
}
