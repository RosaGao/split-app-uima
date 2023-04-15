package com.example.split.Tag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.split.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.os.Bundle;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class TagActivity extends AppCompatActivity {

    private RecyclerView tagsRecyclerView;
    private TagsAdapter tagsAdapter;
    private List<String> tagNames = Arrays.asList("food", "travel", "groceries", "utilities", "business");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tag);

        tagsRecyclerView = findViewById(R.id.tags_recycler_view);
        tagsAdapter = new TagsAdapter(tagNames);
        tagsRecyclerView.setAdapter(tagsAdapter);
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabAddTag = findViewById(R.id.fab_add_tag);
        fabAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Your code for adding a new tag
            }
        });
    }
}
