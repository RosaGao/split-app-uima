package com.example.split.ui.friend;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.split.R;
import com.example.split.databinding.FriendProfileBinding;
import com.example.split.entity.Expense;
import com.example.split.entity.User;
import com.example.split.expenseList.ExpenseListRecyclerViewAdapter;
import com.example.split.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FriendProfileActivity extends AppCompatActivity {

    public static User myFriend = null;
    FriendProfileBinding binding;
    private DatabaseReference dbRef;
    private String userId;


    private List<Expense> expensesWithThisFriend;
    public ExpenseListRecyclerViewAdapter friendProfileExpensesAdapt;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbRef = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding = FriendProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        androidx.appcompat.widget.Toolbar toolBar = binding.friendProfileToolbar;
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (myFriend == null) {
            finish();
        }

        binding.friendEmail.setText(myFriend.getEmail());
        binding.friendName.setText(myFriend.getName());
        binding.friendPhone.setText(myFriend.getPhone());

        dbRef.child("relations").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child(myFriend.getUserId()).exists()) {
                    binding.status.setText("Settled up with " + myFriend.getName());
                    binding.status.setTextColor(getResources().getColor(R.color.black));
                    return;
                }

                double borrowing = snapshot.child(myFriend.getUserId()).getValue(Double.class);
                DecimalFormat df = new DecimalFormat("0.00");
                if (borrowing < 0) { // friend owes me money
                    binding.status.setTextColor(getResources().getColor(R.color.green));
                    binding.status.setText(myFriend.getName() + " owes you $" + df.format((-borrowing)));

                } else if (borrowing > 0) { // I borrowed money from friend
                    binding.status.setTextColor(getResources().getColor(R.color.red));
                    binding.status.setText("You owe " + myFriend.getName() + " $" + df.format(borrowing));
                } else {
                    binding.status.setText("Settled up with " + myFriend.getName());
                    binding.status.setTextColor(getResources().getColor(R.color.black));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        expensesWithThisFriend = getExpensesWithFriend();
        RecyclerView expensesListRecyclerView = binding.expenseListHome;
        friendProfileExpensesAdapt = new ExpenseListRecyclerViewAdapter(null, expensesWithThisFriend, false);
        expensesListRecyclerView.setAdapter(friendProfileExpensesAdapt);

        if (expensesWithThisFriend.size() == 0) {
            binding.expenseWithThemTitle.setText("No expenses with " + myFriend.getName());
        } else {
            binding.expenseWithThemTitle.setText("Expense with " + myFriend.getName());
        }
    }

    private List<Expense> getExpensesWithFriend() {
        List<Expense> allMyExpenses = HomeFragment.allExpenses;
        List<Expense> expensesWithMyFriend = new ArrayList<>();
        String myFriendId = myFriend.getUserId();

        for (Expense exp : allMyExpenses) {
            for (User participant : exp.getParticipants()) {
                if (participant.getUserId().equals(myFriendId)) {
                    expensesWithMyFriend.add(exp);
                }
            }
        }
        return expensesWithMyFriend;
    }


}
