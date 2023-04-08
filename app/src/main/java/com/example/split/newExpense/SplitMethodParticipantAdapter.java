package com.example.split.newExpense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.split.R;
import com.example.split.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SplitMethodParticipantAdapter extends ArrayAdapter<SplitMethodParticipant> {
    int resource;
    SplitMethodActivity activity;

    public SplitMethodParticipantAdapter(Context ctx, int res, List<SplitMethodParticipant> participants) {
        super(ctx, res, participants);
        resource = res;
        activity = (SplitMethodActivity) ctx;
        // fetchUsers();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        SplitMethodParticipant participant = getItem(position);

        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }

        TextView nameView = (TextView) itemView.findViewById(R.id.user_name);
        nameView.setText(participant.getName());

        return itemView;
    }

//    private void fetchUsers() {
//        Query query = FirebaseDatabase.getInstance().getReference("users");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<User> users = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    User user = snapshot.getValue(User.class);
//                    users.add(user);
//                }
//                updateUsers(users);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle error here
//            }
//        });
//    }
//
//
//    private void updateUsers(List<User> users) {
//        clear();
//        addAll(users);
//        notifyDataSetChanged();
//    }
}
