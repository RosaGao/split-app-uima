package com.example.split.ui.friend;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.split.R;
import com.example.split.entity.User;
import com.example.split.expenseList.ExpenseDetailActivity;
import com.example.split.ui.home.HomeFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    Toast toast = Toast.makeText(v.getContext(), "That is you!", Toast.LENGTH_LONG);
                    toast.getView().setBackgroundColor(v.getContext().getResources().getColor(R.color.red));
                    toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                    return;
                }
                Context context = v.getContext();
                Intent intent = new Intent(context, FriendProfileActivity.class);
                FriendProfileActivity.myFriend = user;
                context.startActivity(intent);
            }
        });
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
