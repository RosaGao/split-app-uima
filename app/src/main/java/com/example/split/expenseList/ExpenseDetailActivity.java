package com.example.split.expenseList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.split.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExpenseDetailActivity extends AppCompatActivity {

    TextView expense_name;
    TextView expense_date;
    View tag_view;
    View split_method_view;
    TextView expense_tag;
    TextView expense_method;
    TextView payer_info;
    String current_user;
    String expense_payer;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_detail);
        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.expense_detail_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);

        Intent intent = getIntent();
        String expense_id = intent.getStringExtra("expense_id");
        Log.v("expense id:", expense_id);
        database = FirebaseDatabase.getInstance().getReference().child("expenses").child(expense_id);
        expense_name = findViewById(R.id.expense_name);
        expense_date = findViewById(R.id.expense_timestamp);
        tag_view = findViewById(R.id.expense_tag);
        split_method_view = findViewById(R.id.expense_split_method);
        expense_tag = (TextView) tag_view.findViewById(R.id.tag_layout);
        expense_method = (TextView) split_method_view.findViewById(R.id.split_method_chip);
        payer_info = findViewById(R.id.payer_info);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expense_name.setText(snapshot.child("description").getValue().toString());
                expense_date.setText(snapshot.child("date").getValue().toString());
                expense_tag.setText(snapshot.child("tag").child("name").getValue().toString());
                expense_method.setText(snapshot.child("method").getValue().toString());

                current_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                expense_payer = snapshot.child("payer").child("userId").getValue().toString();
                if (current_user.equals(expense_payer)) {
                    payer_info.setText("You paid $" + snapshot.child("amount").getValue().toString());
                } else {
                    expense_payer = snapshot.child("payer").child("name").getValue().toString();
                    payer_info.setText(expense_payer + " paid $" + snapshot.child("amount").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DBREF:", "getClientr:onCancelled", error.toException());
            }
        });

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
