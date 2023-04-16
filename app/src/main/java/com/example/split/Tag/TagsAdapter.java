package com.example.split.Tag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.split.R;
import com.example.split.entity.Tag;
import com.example.split.ui.tags.TagFragment;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private List<Tag> mTags;
//    private List<Integer> mNumExpenses;

    public TagsAdapter(List<Tag> tags) {
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
        holder.tagName.setText(mTags.get(position).getName());

        // Set the number of expenses
        holder.tagExpenseCount.setText(mTags.get(position).getNumExpenses() + " of expenses");
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

//    public void setTags(List<Tag> tags) {
//        mTags = tags;
//    }

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