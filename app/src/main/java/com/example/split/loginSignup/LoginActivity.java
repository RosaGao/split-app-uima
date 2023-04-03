package com.example.split.loginSignup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.split.R;
import com.example.split.entity.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);


    }

    private void signUp() {
    }

    private void signIn() {
    }

    // TODO finish this activity with auth



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signupButton) {
            signIn();
        } else if (i == R.id.signupButton) {
            signUp();
        }
    }
}
