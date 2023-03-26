package com.example.split.adapter;

import android.widget.ArrayAdapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.split.MainActivity;
import com.example.split.entity.Expense;

public class HomeExpenseAdapter extends ArrayAdapter<Expense> {
    int resource;
    MainActivity mainAct;
    public HomeExpenseAdapter(Context ctx, int res, List<Expense> expenses)
    {
        super(ctx, res, expenses);
        resource = res; // home_expense_item / detail_expense_item
        mainAct = (MainActivity) ctx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        Expense p = getItem(position);

        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }

//        TextView name = (TextView) itemView.findViewById(R.id.expense_name);
//        name.setText(p.getname());
//        TextView size = (TextView) itemView.findViewById(R.id.park_num);
//        size.setText("" + p.getnumber());
//        Button checkin = (Button) itemView.findViewById(R.id.checkin);
//        String availText = p.getchecked_in() ? "Check out" : "Check in";
//        checkin.setText(availText);

//        checkin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (checkin.getText() == "Check out") {
//                    checkin.setText("Check in");
//                    myact.current.setchecked_in(false);
//                    size.setText("" + p.getnumber());
//                    myact.current = null; // no longer checked in anywhere
//                }
//                else {
//                    if (myact.current != null) {
//                        Toast.makeText(myact.getApplicationContext(), myact.getResources().getString(R.string.checkin_toast), Toast.LENGTH_SHORT).show();
//                    } else {
//                        myact.current = p;
//                        p.setchecked_in(true);
//                        size.setText("" + p.getnumber());
//                        checkin.setText("Check out");
//                    }
//                }
//            }
//        });

        return itemView;
    }
}
