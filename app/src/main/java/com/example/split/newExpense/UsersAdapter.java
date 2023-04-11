package com.example.split.newExpense;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.split.R;
import com.example.split.entity.User;

public class UsersAdapter extends ArrayAdapter<User> {
    int resource;
    SelectParticipantsActivity activity;

    public UsersAdapter(Context ctx, int res) {
        super(ctx, res, SelectParticipantsActivity.users);
        Log.v("num users in constructor", String.valueOf(SelectParticipantsActivity.users.size()));
        resource = res;
        activity = (SelectParticipantsActivity) ctx;
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

        TextView name = (TextView) itemView.findViewById(R.id.friend_name);
        name.setText(user.getName());

        CheckBox selected = (CheckBox) itemView.findViewById(R.id.checkBox);
        selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && SelectParticipantsActivity.participants.contains(user)) {
                    SelectParticipantsActivity.participants.remove(user);
                }

                if (isChecked) {
                    SelectParticipantsActivity.participants.add(user);
                }
            }
        });

        return itemView;
    }
}
