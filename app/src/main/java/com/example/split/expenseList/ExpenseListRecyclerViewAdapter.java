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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExpenseListRecyclerViewAdapter
        extends RecyclerView.Adapter<ExpenseViewHolder> {

    private HomeFragment myParentActivity;
    private List<Expense> myExpenses = new ArrayList<>();

    private final View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Expense item = (Expense) view.getTag();

            Context context = view.getContext();
            Intent intent = new Intent(context, ExpenseDetailActivity.class);
            //intent.putExtra(ExpenseDetailActivity.USER_ID, item.getUserId());
            context.startActivity(intent);
        }
    };

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
        Random randomValue = new Random();

        Log.d("in binder", myExpenses.get(position).toString());

        holder.description.setText(myExpenses.get(position).getDescription());
//            holder.tag.setText(myExpenses.get(position).getTag().getNumExpenses());
        holder.date.setText(myExpenses.get(position).getDate());

        if (randomValue.nextInt(10) < 5) {
            holder.status.setText("You borrowed $123");
            holder.status.setTextColor(holder.status.getResources().getColor(R.color.red));
        } else {
            holder.status.setText("You are owed $123");
            holder.status.setTextColor(holder.status.getResources().getColor(R.color.green));
        }


        Log.v("tag name", myExpenses.get(position).getTag().getName());
        holder.tag.setText(myExpenses.get(position).getTag().getName());

        holder.itemView.setOnClickListener(myOnClickListener);
    }

    @Override
    public int getItemCount() {
        return myExpenses.size();
    }
}