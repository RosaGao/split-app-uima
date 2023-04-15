package com.example.split.ui.tags;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.split.R;
import com.example.split.Tag.TagsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TagFragment extends Fragment {

    private DatabaseReference mDatabaseReference;


    private RecyclerView tagsRecyclerView;
    private TagsAdapter tagsAdapter;
    private List<String> tagNames = new ArrayList<>(Arrays.asList("food", "travel", "groceries", "utilities", "business"));

    public TagFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tagsRecyclerView = view.findViewById(R.id.tags_recycler_view);
        tagsAdapter = new TagsAdapter(tagNames);
        tagsRecyclerView.setAdapter(tagsAdapter);
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton fabAddTag = view.findViewById(R.id.fab_add_tag);
        fabAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_add_tag, null);
                builder.setView(dialogView);

                final EditText tagNameInput = dialogView.findViewById(R.id.tag_name_input);

                builder.setTitle("Add a tag")
                        .setPositiveButton("Add Tag", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String tagName = tagNameInput.getText().toString();
                                if (!tagName.isEmpty()) {
                                    tagNames.add(tagName);
                                    tagsAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
