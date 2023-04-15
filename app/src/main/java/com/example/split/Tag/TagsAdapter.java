package com.example.split.Tag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.split.R;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private List<String> mTags;

    public TagsAdapter(List<String> tags) {
        mTags = tags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tagitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the tag name
        holder.tagName.setText(mTags.get(position));

        // Set the number of expenses
        holder.tagExpenseCount.setText("# of expenses");
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public void setTags(List<String> tags) {
        mTags = tags;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tagName;
        public final TextView tagExpenseCount;

        public ViewHolder(@NonNull View view) {
            super(view);
            tagName = view.findViewById(R.id.tag_name);
            tagExpenseCount = view.findViewById(R.id.tag_expense_count);
        }
    }
}