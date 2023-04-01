package com.example.split;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.split.entity.User;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<User> {
    int resource;
    SelectParticipantsActivity activity;


    public UsersAdapter(Context ctx, int res, List<User> users) {
        super(ctx, res, users);
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

        TextView name = (TextView) itemView.findViewById(R.id.participant_name);
        name.setText("Some name");

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

}
