package me.t3sl4.upcortex.UI.Screens.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Screens.Auth.Register.Register1;
import me.t3sl4.upcortex.Util.Utils;

public class AuthSelection extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_selection);

        Utils.hideNavigationBar(this);

        initializeComponents();
        buttonClickListeners();
    }

    private void initializeComponents() {
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
    }

    private void buttonClickListeners() {
        registerButton.setOnClickListener(v -> {
            Intent registerIntent = new Intent(AuthSelection.this, Register1.class);
            startActivity(registerIntent);
        });

        loginButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(AuthSelection.this, Login.class);
            startActivity(loginIntent);
        });
    }
}
