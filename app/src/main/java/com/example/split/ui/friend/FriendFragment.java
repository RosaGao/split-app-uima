package com.example.split.ui.friend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.split.R;
import com.example.split.entity.User;
import com.example.split.ui.friend.FriendViewModel;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FriendFragment extends Fragment {

    private RecyclerView friendList;
    private DatabaseReference databaseReference;
    private FriendsListAdapter friendsListAdapter;
    private List<User> friends;

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        friendList = view.findViewById(R.id.friend_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        friendList.setLayoutManager(layoutManager);

        friends = new ArrayList<>();
        friendsListAdapter = new FriendsListAdapter(friends);
        friendList.setAdapter(friendsListAdapter);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Fetch users from Firebase
        fetchUsersFromFirebase();
    }

    private void fetchUsersFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friends.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        friends.add(user);
                    }
                }
                // Notify the adapter that the data has changed
                friendsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log the error or show a message to the user
            }
        });
    }
}