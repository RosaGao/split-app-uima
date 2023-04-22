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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.split.NewExpenseActivity;
import com.example.split.R;
import com.example.split.entity.Expense;
import com.example.split.entity.Tag;
import com.example.split.entity.User;
import com.example.split.expenseList.ExpenseListRecyclerViewAdapter;
import com.example.split.loginSignup.LoginActivity;
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
    public TagExpenseListRecyclerViewAdapter myAdapt;

    public static Tag myTag = null;
    private TextView tagTitle;
    private TextView tagTotal;
    private List<Expense> expensesWithThisTag = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);

        Intent intent = getIntent();
        tagId = intent.getStringExtra("tag_id");
        if (tagId == null) {
            finish();
        }
        Log.v("tag_id", tagId);

        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.tagToolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTag = null;
                expensesWithThisTag.clear();
                finish();
            }
        });


        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();

        tagTitle = findViewById(R.id.tagTitleTextView);
        tagTotal = findViewById(R.id.totalCalculationTextView);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RecyclerView recyclerView = findViewById(R.id.expense_list_tag);

        dbRef.child("users").child(userId).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.v("num", snapshot.getChildrenCount() + "");
                myTag = snapshot.child(tagId).getValue(Tag.class);

                if (myTag == null) {
                    finish();
                    return;
                }

                tagTitle.setText(myTag.getName());

                getExpensesWithTag();
                myAdapt = new TagExpenseListRecyclerViewAdapter(null, expensesWithThisTag, false);
                recyclerView.setAdapter(myAdapt);

                double total = getTotal(expensesWithThisTag);
                DecimalFormat df = new DecimalFormat("0.00");

                if (total > 0) {
                    tagTotal.setText("You borrowed $" + df.format(total));
                    tagTotal.setTextColor(getResources().getColor(R.color.red));
                } else {
                    tagTotal.setText("You are owed $" + df.format(Math.abs(total)));
                    tagTotal.setTextColor(getResources().getColor(R.color.green));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void getExpensesWithTag() {
        String tagId = myTag.getTagId();

        for (Expense exp : HomeFragment.allExpenses) {
            if (exp.getTag().getTagId().equals(tagId)) {
                expensesWithThisTag.add(exp);
            }
        }
        Log.v("num of expenses w tag", expensesWithThisTag.size() + "");
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
            if (expensesWithThisTag.size() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TagDetailActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_delete, null);
                builder.setView(dialogView);

                builder.setTitle("Delete tag permanently?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference tagDbRef = dbRef.child("users").child(userId).child("tags").child(tagId);
                                    tagDbRef.removeValue();
                                    finish();
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
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Cannot delete tag with expenses!", Toast.LENGTH_LONG);
                toast.getView().setBackgroundColor(getResources().getColor(R.color.red));
                toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
            }

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
