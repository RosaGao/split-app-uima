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

public class UsersAdapter extends ArrayAdapter<User> {
    int resource;
    SelectParticipantsActivity activity;

    public UsersAdapter(Context ctx, int res) {
        super(ctx, res, new ArrayList<>());
        resource = res;
        activity = (SelectParticipantsActivity) ctx;
        fetchUsers();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        User user = getItem(position);

        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }

        TextView name = (TextView) itemView.findViewById(R.id.participant_name);
        name.setText(user.get_name());

        CheckBox selected = (CheckBox) itemView.findViewById(R.id.checkBox);
        selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SelectParticipantsActivity.participants.add(user);
                }
            }
        });

        return itemView;
    }

    private void fetchUsers() {
        Query query = FirebaseDatabase.getInstance().getReference("<YOUR_USERS_NODE>");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }
                updateUsers(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error here
            }
        });
    }


    private void updateUsers(List<User> users) {
        clear();
        addAll(users);
        notifyDataSetChanged();
    }
}
