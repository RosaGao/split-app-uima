package com.example.split.loginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.split.R;
import com.example.split.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText emailEditText, phoneNumberEditText, passwordEditText, confirmPasswordEditText, displayNameEditText;
    private Button finishedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        // Initialize FirebaseAuth and DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        emailEditText = findViewById(R.id.email_address);
        phoneNumberEditText = findViewById(R.id.phone_number);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        displayNameEditText = findViewById(R.id.display_name);
        finishedButton = findViewById(R.id.finish_button);

        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void signUp() {
        if (!validateForm()) {
            return;
        }

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            String phoneNumber = phoneNumberEditText.getText().toString();
                            String displayName = displayNameEditText.getText().toString();
                            String userId = user.getUid(); // Use the user's uid as userId
                            writeNewUser(userId, displayName, email, phoneNumber, password);

                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Exception exception = task.getException();
                            String message = "Sign Up Failed.";
                            if (exception != null && exception.getMessage() != null) {
                                message = exception.getMessage();
                            }
                            Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void writeNewUser(String userId, String name, String email, String phoneNumber, String password) {
        User user = new User(name, email, phoneNumber, password);
        user.setUserId(userId);
        mDatabase.child("users").child(userId).updateChildren(user.toMap());
    }

    private boolean validateForm() {
        boolean valid = true;

        // Validate email
        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Required.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        // Validate phone number
        String phoneNumber = phoneNumberEditText.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberEditText.setError("Required.");
            valid = false;
        } else {
            phoneNumberEditText.setError(null);
        }

        // Validate password
        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Required.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        // Validate confirm password
        String confirmPassword = confirmPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.setError("Required.");
            valid = false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match.");
            valid = false;
        } else {
            confirmPasswordEditText.setError(null);
        }

        // Validate display name
        String displayName = displayNameEditText.getText().toString();
        if (TextUtils.isEmpty(displayName)) {
            displayNameEditText.setError("Required.");
            valid = false;
        } else {
            displayNameEditText.setError(null);
        }

        return valid;
    }
}
