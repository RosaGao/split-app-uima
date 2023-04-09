package com.example.split.newExpense;

import static java.lang.Math.round;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.split.NewExpenseActivity;
import com.example.split.R;
import com.example.split.entity.SplitMethod;
import com.example.split.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SplitMethodParticipantAdapter extends ArrayAdapter<User> {
    int resource;
    SplitMethodActivity activity;

    List<User> participantsToSplit;


    public SplitMethodParticipantAdapter(Context ctx, int res, List<User> participantsToSplit) {
        super(ctx, res, participantsToSplit);
        resource = res;
        activity = (SplitMethodActivity) ctx;
        this.participantsToSplit = participantsToSplit;
    }

    @SuppressLint("ResourceAsColor")
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

        TextView nameView = (TextView) itemView.findViewById(R.id.user_name);
        nameView.setText(participant.getName());

        EditText edit = (EditText) itemView.findViewById(R.id.inputtextedit);

        TextView sign = (TextView) itemView.findViewById(R.id.dollarOrPercent);
        if (SplitMethodActivity.method == SplitMethod.PERCENT) {
            sign.setText("%");
        } else {
            sign.setText("$");
        }

        if (SplitMethodActivity.method == SplitMethod.EQUAL) {
            Double each = Double.parseDouble(SplitMethodActivity.amount) / participantsToSplit.size();
            DecimalFormat df = new DecimalFormat("#.##");
            edit.setText(df.format(each));
            edit.setFocusable(false);
            edit.setFocusableInTouchMode(false);
        } else {
            edit.setText("");
            edit.setFocusable(true);
            edit.setFocusableInTouchMode(true);
        }

        return itemView;
    }
}