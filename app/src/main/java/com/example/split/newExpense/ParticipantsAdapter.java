package com.example.split.newExpense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.split.NewExpenseActivity;
import com.example.split.R;
import com.example.split.entity.User;

import java.util.List;


public class ParticipantsAdapter extends ArrayAdapter<User> {
    int resource;
    SelectPayerActivity activity;

    CompoundButton prevSelected = null;

    public ParticipantsAdapter(Context ctx, int res, List<User> participants) {
        super(ctx, res, participants);
        resource = res;
        activity = (SelectPayerActivity) ctx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        User participant = getItem(position);

        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }

        TextView name = (TextView) itemView.findViewById(R.id.participant_name);
        name.setText(participant.getName());

        RadioButton selected = (RadioButton) itemView.findViewById(R.id.radioButton);
        selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (NewExpenseActivity.finalPayer == null || prevSelected == null) {
                        NewExpenseActivity.finalPayer = participant;
                        prevSelected = buttonView;
                    }
                    else {
                        if (NewExpenseActivity.finalPayer != participant) {
                            prevSelected.setChecked(false);
                            prevSelected = buttonView;
                            NewExpenseActivity.finalPayer = participant;
                        } else {
                            prevSelected.setChecked(false);
                            prevSelected = null;
                            NewExpenseActivity.finalPayer = null;
                        }
                    }
                }
            }
        });

        return itemView;
    }
}
