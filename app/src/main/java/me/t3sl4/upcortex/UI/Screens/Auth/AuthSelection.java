package me.t3sl4.upcortex.UI.Screens.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.UI.Screens.Auth.Register.Register1;
import me.t3sl4.upcortex.UI.Screens.General.Dashboard;
import me.t3sl4.upcortex.Utility.HTTP.Requests.Auth.AuthService;
import me.t3sl4.upcortex.Utility.Screen.ScreenUtil;

public class AuthSelection extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;
    private Button lansmanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_selection);

        ScreenUtil.hideNavigationBar(this);

        initializeComponents();
        buttonClickListeners();
    }

    private void initializeComponents() {
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
        lansmanButton = findViewById(R.id.lansmanButton);
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
        lansmanButton.setOnClickListener(v -> {
            sendLoginRequest();
        });
    }

    private void sendLoginRequest() {
        String idNumberText = "00000000000";
        String passwordText = "dinamikbeyin";

        if (idNumberText.isEmpty() || passwordText.isEmpty()) {
            Sneaker.with(AuthSelection.this)
                    .setTitle(getString(R.string.error_title))
                    .setMessage(getString(R.string.error_empty_fields))
                    .sneakError();
            return;
        }

        if (idNumberText.length() != 11) {
            Sneaker.with(AuthSelection.this)
                    .setTitle(getString(R.string.error_title))
                    .setMessage(getString(R.string.error_invalid_id_number))
                    .sneakError();
            return;
        }

        AuthService.login(this, idNumberText, passwordText,
                () -> {
                    Intent dashboardIntent = new Intent(AuthSelection.this, Dashboard.class);
                    startActivity(dashboardIntent);
                    finish();
                },
                () -> {
                    Sneaker.with(AuthSelection.this)
                            .setTitle(getString(R.string.error_title))
                            .setMessage(getString(R.string.error_login_failed))
                            .sneakError();
                }
        );
    }
}
