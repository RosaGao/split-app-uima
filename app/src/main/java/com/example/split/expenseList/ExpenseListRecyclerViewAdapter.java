package com.example.split.expenseList;

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
import com.example.split.ui.home.HomeFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExpenseListRecyclerViewAdapter
        extends RecyclerView.Adapter<ExpenseViewHolder> {

    private HomeFragment myParentActivity;
    private List<Expense> myExpenses = new ArrayList<>();

    /*private final View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Context context = view.getContext();
            Intent intent = new Intent(context, ExpenseDetailActivity.class);
            //intent.putExtra("expense_id", myExpenses.get(1).getExpenseId());
            //intent.putExtra("expense_id", item.getExpenseId());
            //intent.putExtra("expense_id", context.getText());
            context.startActivity(intent);
        }
    };*/

    public ExpenseListRecyclerViewAdapter(HomeFragment parent,
                                          List<Expense> allExpenses,
                                          boolean twoPane) {
        myExpenses = allExpenses;
        myParentActivity = parent;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_list_content, parent, false);
        return new ExpenseViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {

        holder.description.setText(myExpenses.get(position).getDescription());
        holder.date.setText(myExpenses.get(position).getDate());

        Expense expense = myExpenses.get(position);
        double borrowing = expense.getBorrowing();
        DecimalFormat df = new DecimalFormat("0.00");

        if (borrowing > 0) {
            holder.status.setText("You borrowed $" + df.format(borrowing));
            holder.status.setTextColor(holder.status.getResources().getColor(R.color.red));
        } else {
            holder.status.setText("You are owed $" + df.format(Math.abs(borrowing)));
            holder.status.setTextColor(holder.status.getResources().getColor(R.color.green));
        }

        Log.v("tag name", myExpenses.get(position).getTag().getName());
        holder.tag.setText(myExpenses.get(position).getTag().getName());

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