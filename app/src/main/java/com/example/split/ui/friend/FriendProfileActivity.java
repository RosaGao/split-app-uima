package com.example.split.ui.friend;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.split.R;
import com.example.split.databinding.FriendProfileBinding;
import com.example.split.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class FriendProfileActivity extends AppCompatActivity {

    public static User myFriend = null;
    FriendProfileBinding binding;
    private DatabaseReference dbRef;
    private String userId;



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
                double borrowing = snapshot.child(myFriend.getUserId()).getValue(Double.class);
                DecimalFormat df = new DecimalFormat("0.00");
                if (borrowing < 0) { // friend owes me money
                    binding.status.setTextColor(getResources().getColor(R.color.green));
                    binding.status.setText(myFriend.getName() + " owes you $" + df.format((-borrowing)));

                } else { // I borrowed money from friend
                    binding.status.setTextColor(getResources().getColor(R.color.red));
                    binding.status.setText("You owe " + myFriend.getName() + " $" + df.format(borrowing));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
