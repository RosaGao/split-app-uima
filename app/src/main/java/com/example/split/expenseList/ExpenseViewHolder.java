package com.example.split.expenseList;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.split.R;
import com.google.android.material.chip.Chip;

class ExpenseViewHolder extends RecyclerView.ViewHolder {

    TextView description;
    Chip tag;
    TextView date;
    TextView status;

    public ExpenseViewHolder(@NonNull View itemView) {
        super(itemView);
        description = (TextView) itemView.findViewById(R.id.expense_description_home_TextView);
        tag = (Chip) itemView.findViewById(R.id.tag_layout);
        date = (TextView) itemView.findViewById(R.id.expense_date_home_TextView);
        status = (TextView) itemView.findViewById(R.id.expense_status_home_TextView);
    }
}