package com.example.split.expenseList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.split.R;
import com.example.split.Tag.TagDetailActivity;
import com.example.split.entity.Expense;
import com.example.split.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    Context context;

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
    public void onBindViewHolder(@NonNull ExpenseDetailViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String payee_id = payee_list.get(position).getUserId();
        String current_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String payer_id = expense.getPayer().getUserId();
        borrowing = expense.getBorrowing(payee_id);
        String display_amount = String.format("%.2f", Math.abs(borrowing));
        holder.icon_left.setVisibility(View.INVISIBLE);
        holder.icon_right.setVisibility(View.INVISIBLE);
        if(payee_list.get(position).getUserId().equals(current_user)) {
            holder.payee_info.setText("You share $" + display_amount);
        } else {
            holder.payee_info.setText(payee_list.get(position).getName() + " shares $" + display_amount);
        }
        if(payer_id.equals(payee_id)) {
            holder.icon_left.setImageResource(R.drawable.payer_icon);
            holder.icon_left.setVisibility(View.VISIBLE);
        } else if(current_user.equals(payer_id) && borrowing != 0.0) {
            holder.icon_left.setImageResource(R.drawable.notify_icon);
            holder.icon_left.setVisibility(View.VISIBLE);
            holder.icon_right.setImageResource(R.drawable.settle_icon);
            holder.icon_right.setVisibility(View.VISIBLE);
            holder.icon_right.setClickable(true);
            holder.icon_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ADD CONFIRMATION DIALOGUE
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Are you sure you want to settle with " + payee_list.get(position).getName() + "?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            holder.icon_right.setImageResource(R.drawable.settled_icon);
                            expense.settle(payee_id);
                            dbref.child("relations").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    }
                                    else {
                                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                        double new_relation = task.getResult().child(payee_id).child(payer_id).getValue(Double.class);
                                        new_relation -= Math.abs(borrowing);
                                        dbref.child("relations").child(payee_id).child(payer_id).setValue(new_relation);
                                        dbref.child("relations").child(payer_id).child(payee_id).setValue(new_relation * -1.0);
                                    }
                                }
                            });
                            dbref.child("expenses").child(expense.getExpenseId()).updateChildren(expense.toMap());
                            holder.payee_image.setAlpha(69);
                            holder.payee_info.setTextColor(Color.GRAY);
                            holder.icon_left.setAlpha(69);
                            holder.icon_right.setClickable(false);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {}
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        } else if(current_user.equals(payer_id) && borrowing == 0.0) {
            holder.icon_left.setImageResource(R.drawable.notify_icon);
            holder.icon_left.setVisibility(View.VISIBLE);
            holder.icon_right.setImageResource(R.drawable.settled_icon);
            holder.icon_right.setVisibility(View.VISIBLE);
            holder.payee_image.setAlpha(69);
            holder.payee_info.setTextColor(Color.GRAY);
            holder.payee_info.setText(payee_list.get(position).getName() + " is settled.");
            holder.icon_left.setAlpha(69);
        } else if(payee_id.equals(current_user)){
            holder.icon_left.setImageResource(R.drawable.you_icon);
            holder.icon_left.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.expense.getParticipants().size();
    }

}