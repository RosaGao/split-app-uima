package com.example.split.ui.friend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.split.R;
import com.example.split.entity.User;

import java.util.List;
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    private List<User> friendsList;

    public FriendsListAdapter(List<User> friendsList) {
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = friendsList.get(position);
        holder.friendName.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView friendImg;
        TextView friendName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendImg = itemView.findViewById(R.id.friend_img);
            friendName = itemView.findViewById(R.id.friend_name);
        }
    }
}
