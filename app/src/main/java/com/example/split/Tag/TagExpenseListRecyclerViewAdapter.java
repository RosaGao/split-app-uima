package com.example.split.Tag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.split.R;
import com.example.split.entity.Expense;
import com.example.split.expenseList.ExpenseDetailActivity;
import com.example.split.Tag.TagExpenseViewHolder;
import com.example.split.ui.home.HomeFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TagExpenseListRecyclerViewAdapter
        extends RecyclerView.Adapter<TagExpenseViewHolder> {

    private TagDetailActivity myParentActivity;
    private List<Expense> myExpenses = new ArrayList<>();
    public TagExpenseListRecyclerViewAdapter(TagDetailActivity parent, List<Expense> allExpenses, boolean twoPane) {
        myExpenses = allExpenses;
        myParentActivity = parent;
    }

    @NonNull
    @Override
    public TagExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_expense_layout, parent, false);
        return new TagExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagExpenseViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.description.setText(myExpenses.get(position).getDescription());
        holder.date.setText(myExpenses.get(position).getDate());

        Expense expense = myExpenses.get(position);
        double borrowing = expense.getBorrowing();
        DecimalFormat df = new DecimalFormat("0.00");

        if (borrowing > 0) {
            holder.status.setText("You borrowed $" + df.format(borrowing));
            holder.status.setTextColor(holder.status.getResources().getColor(R.color.red));
        } else if (borrowing < 0) {
            holder.status.setText("You are owed $" + df.format(Math.abs(borrowing)));
            holder.status.setTextColor(holder.status.getResources().getColor(R.color.green));
        } else {
            holder.status.setText("You are settled.");
            holder.status.setTextColor(holder.status.getResources().getColor(R.color.black));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, ExpenseDetailActivity.class);
                intent.putExtra("expense_id", myExpenses.get(position).getExpenseId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myExpenses.size();
    }

}