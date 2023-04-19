package com.example.split.expenseList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.split.R;

public class ExpenseDetailViewHolder extends RecyclerView.ViewHolder{

    TextView payee_info;
    ImageView icon_left;
    ImageView icon_right;
    public ExpenseDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        payee_info = (TextView) itemView.findViewById(R.id.payee_info);
        icon_left = (ImageView) itemView.findViewById(R.id.icon_slot_1);
        icon_right = (ImageView) itemView.findViewById(R.id.icon_slot_2);
    }
}
