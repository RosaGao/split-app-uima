package com.example.split.ui.tags;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagViewModel extends ViewModel {

    private MutableLiveData<List<String>> tags;

    public TagViewModel() {
        tags = new MutableLiveData<>(new ArrayList<>(Arrays.asList("food", "travel", "groceries", "utilities", "business")));
    }

    public LiveData<List<String>> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        List<String> currentTags = tags.getValue();
        currentTags.add(tag);
        tags.setValue(currentTags);
    }
}
