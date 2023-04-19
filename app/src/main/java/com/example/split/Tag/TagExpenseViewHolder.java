package com.example.split.Tag;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.split.R;
import com.google.android.material.chip.Chip;

class TagExpenseViewHolder extends RecyclerView.ViewHolder {

    TextView description;
    TextView date;
    TextView status;

    public TagExpenseViewHolder(@NonNull View itemView) {
        super(itemView);
        description = (TextView) itemView.findViewById(R.id.expense_description_home_TextView);
        date = (TextView) itemView.findViewById(R.id.expense_date_home_TextView);
        status = (TextView) itemView.findViewById(R.id.expense_status_home_TextView);
    }
}