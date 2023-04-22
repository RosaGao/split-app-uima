package com.example.split.expenseList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.split.R;
import com.example.split.entity.Expense;
import com.example.split.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ExpenseDetailRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseDetailViewHolder> {

    List<User> payee_list;
    Expense expense;
    private DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    private double borrowing = 0.0;

    public ExpenseDetailRecyclerViewAdapter(Expense expense) {
        this.expense = expense;
        this.payee_list = expense.getParticipants();
    }

    @NonNull
    @Override
    public ExpenseDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_detail_payee_layout, parent, false);
        return new ExpenseDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseDetailViewHolder holder, int position) {
        String payee_id = payee_list.get(position).getUserId();
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                borrowing = snapshot.child(expense.getExpenseId()).child(payee_id).getValue(Double.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        Double amount = Math.abs(borrowing);
        String display_amount = String.format("%.2f", amount);
        String current_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String payer_id = expense.getPayer().getUserId();
        if(payee_list.get(position).getUserId().equals(current_user)) {
            holder.payee_info.setText("You share $" + display_amount);
        } else {
            holder.payee_info.setText(payee_list.get(position).getName() + " shares $" + display_amount);
        }
        if(payer_id.equals(payee_id)) {
            holder.icon_left.setImageResource(R.drawable.payer_icon);
            holder.icon_left.setVisibility(View.VISIBLE);
        }
        if(current_user.equals(payer_id)) {
            holder.icon_left.setImageResource(R.drawable.notify_icon);
            holder.icon_left.setVisibility(View.VISIBLE);
            holder.icon_right.setImageResource(R.drawable.settle_icon);
            holder.icon_right.setVisibility(View.VISIBLE);
            holder.icon_right.setClickable(true);
            holder.icon_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.icon_right.setColorFilter(R.color.green);
                }
            });
        } else if(payee_id.equals(current_user)){
            holder.icon_left.setImageResource(R.drawable.you_icon);
            holder.icon_right.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.expense.getParticipants().size();
    }

}