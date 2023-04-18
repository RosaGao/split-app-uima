package com.example.split.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.split.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    private EditText nameField;
    private EditText phoneField;
    private EditText emailField;
    private EditText passwordField;
    private Button saveButton;

    private DatabaseReference databaseReference;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the ViewModel
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Get references to all EditText fields and the Save button
        nameField = view.findViewById(R.id.name_field);
        phoneField = view.findViewById(R.id.phone_field);
        emailField = view.findViewById(R.id.email_field);
        passwordField = view.findViewById(R.id.password_field);
        saveButton = view.findViewById(R.id.save_button);

        // Get current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get reference to the current user's data in the Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        // Listen for changes to the user's data in the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get user's data
                String name = dataSnapshot.child("name").getValue(String.class);
                String phone = dataSnapshot.child("phone").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String password = dataSnapshot.child("password").getValue(String.class);

                // Populate EditText fields with user's data
                nameField.setText(name);
                phoneField.setText(phone);
                emailField.setText(email);
                passwordField.setText(password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Set up Save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update user's data in the database
                String name = nameField.getText().toString().trim();
                String phone = phoneField.getText().toString().trim();
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                databaseReference.child("name").setValue(name);
                databaseReference.child("phone").setValue(phone);
                databaseReference.child("email").setValue(email);
                databaseReference.child("password").setValue(password);
            }
        });
    }
}
