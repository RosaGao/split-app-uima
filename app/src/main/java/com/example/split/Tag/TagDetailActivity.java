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

import java.util.ArrayList;
import java.util.List;

public class TagDetailActivity extends AppCompatActivity {

    private static final int LAUNCH_NEW_EXPENSE_REQUEST_CODE = 1000;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    DatabaseReference userDataRef;
    private static String userId;
    private static User currentUser = null;
    public static List<Expense> allExpenses = new ArrayList<>();
    public TagExpenseListRecyclerViewAdapter myAdapt;

    public static Tag myTag = null;
    private TextView tagTitle;
    private TextView tagTotal;
    private List<Expense> expensesWithThisTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.v("userid", userId);
        userDataRef = dbRef.child("users").child(userId);

        tagTitle = findViewById(R.id.tagTitleTextView);
        tagTotal = findViewById(R.id.totalCalculationTextView);

        tagTitle.setText(myTag.getName());
        // TODO: can't seem to find total in tag, might need to calculate
//        tagTotal.setText(myTag.);

        // TODO: not sure if mine is actually called "expenseList". need to reviw
        userDataRef.child("expenseList").addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0) {
                    findViewById(R.id.emptyHomeView).setVisibility(View.VISIBLE);
                    Log.v("tag expenses", "no expenses yet");
                    return;
                } else {
                    findViewById(R.id.emptyHomeView).setVisibility(View.INVISIBLE);

                    List<Expense> expenses = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Expense exp = ds.getValue(Expense.class);
                        expenses.add(exp);
                    }

                    // TODO: is this line necessary again? why
                    findViewById(R.id.emptyHomeView).setVisibility(View.INVISIBLE);
                    allExpenses.clear();
                    allExpenses.addAll(expenses);
                    myAdapt.notifyDataSetChanged();

                    Log.v("user expenses", String.valueOf(allExpenses.size()));

                    // TODO: set color of total text, maybe refer to friend profile
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // failed to read value
                Log.w("db expenses: " + userId, "Failed to read value.", error.toException());
            }
        }));

        expensesWithThisTag = getExpensesWithTag();
        RecyclerView recyclerView = findViewById(R.id.expense_list_tag);
        // TODO: not sure if parent should be this or null
        myAdapt = new TagExpenseListRecyclerViewAdapter(this, expensesWithThisTag, false);
        recyclerView.setAdapter(myAdapt);

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

            List<Expense> allMyExpenses = HomeFragment.allExpenses;
            String tagId = myTag.getTagId();

            for (Expense exp : allMyExpenses) {
                if (exp.getTag().getTagId().equals(tagId)) {
                    // TODO: remove expenses with this tag, i don't think this is right
                    exp = null;
                }
            }
            // TODO: have to grab expense id and iterate through all expenses in database to remove

            // TODO: after deleting tag, should return user (from tag detail) to tag page
//            Intent sentResultToHome = new Intent(NewExpenseActivity.this, HomeFragment.class);
//            setResult(Activity.RESULT_OK, sentResultToHome);
            finish();
            return true;
        } else if (item.getItemId() == R.id.edit_image_button) {
//            // TODO: not sure why requireContext and requireActivity are creating errors, otherwise this should be correct
//            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//            LayoutInflater inflater = requireActivity().getLayoutInflater();
//            View dialogView = inflater.inflate(R.layout.dialog_add_tag, null);
//            builder.setView(dialogView);
//
//            final EditText tagNameInput = dialogView.findViewById(R.id.tag_name_input);
//
//            builder.setTitle("Edit tag name")
//                    .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            String tagName = tagNameInput.getText().toString();
//                            if (!tagName.isEmpty()) {
//                                // TODO: this is such a dumb question, but do i need to save changes to the database or is it automatic?
//                                myTag.setName(tagName);
//                            }
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
        }

        return false;
    }
}
