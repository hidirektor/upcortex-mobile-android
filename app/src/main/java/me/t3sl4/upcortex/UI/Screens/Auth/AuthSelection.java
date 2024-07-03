package me.t3sl4.upcortex.UI.Screens.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;

public class AuthSelection extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_selection);

        NavigationBarUtil.hideNavigationBar(this);

        initializeComponents();
        buttonClickListeners();
    }

    private void initializeComponents() {
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
    }

    private void buttonClickListeners() {
        registerButton.setOnClickListener(v -> {

        });

        loginButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(AuthSelection.this, Login.class);
            startActivity(loginIntent);
        });
    }
}
