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

import java.util.List;

public class ExpenseDetailRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseDetailViewHolder> {

    List<User> payee_list;
    Expense expense;

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
        int amount = Integer.parseInt(expense.getAmount()) / expense.getParticipants().size();
        String current_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(payee_list.get(position).getUserId().equals(current_user)) {
            holder.payee_info.setText("You share $" + amount);
        } else {
            holder.payee_info.setText(payee_list.get(position).getName() + " shares $" + amount);
        }
        if(expense.getPayer().getUserId().equals(payee_list.get(position).getUserId())) {
            holder.icon_left.setImageResource(R.drawable.payer_icon);
            holder.icon_left.setVisibility(View.VISIBLE);
            //holder.desc_left.setText("Payer");
        } else if(expense.isSettled(payee_list.get(position))) {
            holder.icon_left.setImageResource(R.drawable.notify_icon);
            holder.icon_left.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.expense.getParticipants().size();
    }

}