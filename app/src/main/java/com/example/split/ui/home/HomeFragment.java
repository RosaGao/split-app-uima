package com.example.split.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.split.MainActivity;
import com.example.split.NewExpenseActivity;
import com.example.split.R;
import com.example.split.entity.Expense;
import com.example.split.entity.User;
import com.example.split.expenseList.ExpenseListRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static final int LAUNCH_NEW_EXPENSE_REQUEST_CODE = 1000;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    DatabaseReference userDataRef;

    private static String userId ;
    public static User currentUser = null;
    public static List<Expense> allExpenses = new ArrayList<>();
    public ExpenseListRecyclerViewAdapter myAdapt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();

        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.new_expense_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewExpense = new Intent(getActivity(), NewExpenseActivity.class);
                addNewExpense.putExtra("userId", userId);
                startActivityForResult(addNewExpense, LAUNCH_NEW_EXPENSE_REQUEST_CODE);
            }
        });

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDataRef = dbRef.child("users").child(userId);

        RecyclerView recyclerView = view.findViewById(R.id.expense_list_home);
        assert recyclerView != null;
        myAdapt = new ExpenseListRecyclerViewAdapter(this, allExpenses, false);
        recyclerView.setAdapter(myAdapt);

        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null) {
                    Log.e("expense list activity: ", "User " + userId + " is unexpectedly null");
                } else {
                    Log.v("user name", String.valueOf(user.getName()));
                    Log.v("user email", String.valueOf(user.getEmail()));

                    if (user.getExpenseList() == null) {
                        view.findViewById(R.id.emptyHomeView).setVisibility(View.VISIBLE);
                        Log.v("user expenses", "no expenses yet");
                        return;
                    }


                    view.findViewById(R.id.emptyHomeView).setVisibility(View.INVISIBLE);
                    allExpenses.clear();
                    allExpenses.addAll(user.getExpenseList());
                    myAdapt.notifyDataSetChanged();
                    currentUser = user;
                    Log.v("user expenses", String.valueOf(allExpenses.size()));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("db expenses " + userId, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_NEW_EXPENSE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                myAdapt.notifyDataSetChanged();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/users/" + userId + "/expenseList", allExpenses);
                dbRef.updateChildren(childUpdates);
            }
        } else {
            Snackbar.make(getView(), "Failed to create new expense!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}