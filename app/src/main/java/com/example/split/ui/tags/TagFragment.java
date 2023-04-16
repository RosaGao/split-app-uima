package com.example.split.ui.tags;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.split.R;
import com.example.split.Tag.TagsAdapter;
import com.example.split.entity.Tag;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagFragment extends Fragment {

    private DatabaseReference mDatabaseReference;
    private String userId;

    private RecyclerView tagsRecyclerView;
    private TagsAdapter tagsAdapter;
    public static List<Tag> tagList = new ArrayList<>();

    public TagFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return inflater.inflate(R.layout.fragment_tag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tagsRecyclerView = view.findViewById(R.id.tags_recycler_view);
        tagsAdapter = new TagsAdapter(tagList);
        tagsRecyclerView.setAdapter(tagsAdapter);
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabaseReference.child("users").child(userId).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tagList.clear();
                for (DataSnapshot tagSnapshot : dataSnapshot.getChildren()) {
                    Tag tag = tagSnapshot.getValue(Tag.class);
                    Log.v("tag", tag.getName() + ", num ->" + tag.getNumExpenses());
                    tagList.add(tag);
                }
                tagsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TagFragment", "Error fetching tags: ", databaseError.toException());
            }
        });

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
                                    saveTagToDatabase(tagName);
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


    private void saveTagToDatabase(String tagName) {
        DatabaseReference tagDbRef = mDatabaseReference.child("users").child(userId).child("tags");
        String tagId = tagDbRef.push().getKey();
        tagDbRef.child(tagId).child("name").setValue(tagName);
        tagDbRef.child(tagId).child("numExpenses").setValue(0);
    }
}

