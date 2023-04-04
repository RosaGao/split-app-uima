package com.example.split.expenseList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.split.NewExpenseActivity;
import com.example.split.R;
import com.example.split.databinding.ActivityMainBinding;
import com.example.split.entity.Expense;
import com.example.split.entity.SplitMethod;
import com.example.split.entity.Tag;
import com.example.split.entity.User;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {
    private static final int LAUNCH_NEW_EXPENSE_REQUEST_CODE = 1000;


    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    DatabaseReference userDataRef;

    private static String userId;
    public List<Expense> allExpenses = new ArrayList<>();
    public SimpleItemRecyclerViewAdapter myAdapt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDataRef = dbRef.child(userId);
        Log.v("user id", userId);

        RecyclerView recyclerView = findViewById(R.id.expense_list_home);
        assert recyclerView != null;
        myAdapt = new SimpleItemRecyclerViewAdapter(this, allExpenses, false);
        recyclerView.setAdapter(myAdapt);

        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null) {
                    //TODO login activity if user is null

                    Log.e("expense list activity: ", "User " + userId + " is unexpectedly null");
                    Toast.makeText(getApplicationContext(),
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    allExpenses.clear();
                    allExpenses.addAll(user.get_expenses());
                }
                myAdapt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("db expenses " + userId, "Failed to read value.", error.toException());
            }
        });





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_expense_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get details for a new Client, using dummy values for now
//                Expense newExpense = new Expense(userId, "description", new Date().toString(), "$100", new ArrayList<>(), new User("rosa", "email", "phone", "password"), new Tag("id", "tagname"), SplitMethod.EXACT);
//                allExpenses.add(newExpense);
//                myAdapt.notifyDataSetChanged();
//
//                String key = dbRef.child("expenses").push().getKey();
//                newExpense.setExpenseId(key);
//                dbRef.child("expenses").child(key).setValue(newExpense);
//                dbRef.child("users").child(userId).child("expense_list").setValue(allExpenses);
//
//                Snackbar.make(view, "New Expense added", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent addNewExpense = new Intent(ExpenseListActivity.this, NewExpenseActivity.class);
                addNewExpense.putExtra("userId", userId);
                startActivityForResult(addNewExpense, LAUNCH_NEW_EXPENSE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_NEW_EXPENSE_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK && data.getStringExtra("expenseId") != null){
                String expenseId =data.getStringExtra("expenseId");
                dbRef.child("expenses").child(expenseId).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Expense newExpense = snapshot.getValue(Expense.class);
                        allExpenses.add(newExpense);
                        myAdapt.notifyDataSetChanged();

                        dbRef.child("users").child(userId).child("expense_list").setValue(allExpenses);
                        Snackbar.make(getWindow().getDecorView().getRootView(), "New Expense added!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            } else {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Failed to create new expense!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private ExpenseListActivity myParentActivity;
        private List<Expense> myExpenses = new ArrayList<>();

        private final View.OnClickListener myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Expense item = (Expense) view.getTag();

                Context context = view.getContext();
                Intent intent = new Intent(context, ExpenseDetailActivity.class);
                intent.putExtra(ExpenseDetailActivity.USER_ID, item.getUserId());
                context.startActivity(intent);
            }
        };


        SimpleItemRecyclerViewAdapter(ExpenseListActivity parent,
                                      List<Expense> allExpenses,
                                      boolean twoPane) {
            myExpenses = allExpenses;
            myParentActivity = parent;
        }

        @NonNull
        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_expense_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            Log.d("in binder", myExpenses.get(position).toString());

            holder.description.setText(myExpenses.get(position).getDescription());
//            holder.tag.setText(myExpenses.get(position).getTag().getNumExpenses());
            holder.date.setText(myExpenses.get(position).getDate().toString());
            holder.status.setText("You owe $123");

            holder.itemView.setTag(myExpenses.get(position));
            holder.itemView.setOnClickListener(myOnClickListener);
        }

        @Override
        public int getItemCount() {
            return myExpenses.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView description;
            private Chip tag;
            private TextView date;
            private TextView status;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                description = (TextView) itemView.findViewById(R.id.expense_description_home_TextView);
                tag = (Chip) itemView.findViewById(R.id.tag_layout);
                date = (TextView) itemView.findViewById(R.id.expense_date_home_TextView);
                status = (TextView) itemView.findViewById(R.id.expense_status_home_TextView);
            }
        }
    }
}
