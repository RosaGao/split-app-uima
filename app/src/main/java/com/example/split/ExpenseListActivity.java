package com.example.split;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.split.entity.Expense;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private List<Expense> allExpenses;
    private SimpleItemRecyclerViewAdapter myAdapt;

    // TODO

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, allExpenses, false));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private ExpenseListActivity myParentActivity;
        private List<Expense> myExpenses;

        private final View.OnClickListener myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Expense item = (Expense) view.getTag();

                Context context = view.getContext();
                Intent intent = new Intent(context, ExpenseListActivity.class);
//                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.getId());
                context.startActivity(intent);
            }
        };


        SimpleItemRecyclerViewAdapter(ExpenseListActivity parent,
                                      List<Expense> allExpenses,
                                      boolean twoPane) {
            myExpenses = allExpenses;
            myParentActivity = parent;
        }

        @NonNull
        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_expense_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            Log.d("in binder", myExpenses.get(position).toString());

            holder.description.setText(myExpenses.get(position).getDescription());
            holder.tag.setText(myExpenses.get(position).getTag().getNumExpenses());
            holder.date.setText(myExpenses.get(position).getDate().toString());
            holder.status.setText("You owe $123");

            holder.itemView.setTag(myExpenses.get(position));
            holder.itemView.setOnClickListener(myOnClickListener);
        }

        @Override
        public int getItemCount() {
            return myExpenses.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView description;
            private Chip tag;
            private TextView date;
            private TextView status;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                description = (TextView) itemView.findViewById(R.id.expense_description_home_TextView);
                tag = (Chip) itemView.findViewById(R.id.tag_layout);
                date = (TextView) itemView.findViewById(R.id.expense_date_home_TextView);
                status = (TextView) itemView.findViewById(R.id.expense_status_home_TextView);
            }
        }
    }
}
