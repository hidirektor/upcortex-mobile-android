package me.t3sl4.upcortex.UI.Screens.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Screens.Auth.Register.Register1;
import me.t3sl4.upcortex.UI.Screens.General.Dashboard;
import me.t3sl4.upcortex.UI.Screens.ResetPass.ResetPass1;
import me.t3sl4.upcortex.Util.Utils;

public class Login extends AppCompatActivity {

    private TextInputEditText idNumber;
    private TextInputEditText password;

    private TextView forgetPassword;
    private Button loginButton;
    private LinearLayout registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Utils.hideNavigationBar(this);

        initializeComponents();
        buttonClickListeners();
        Utils.passwordListener(password);
    }

    private void initializeComponents() {
        idNumber = findViewById(R.id.editTextIDNumber);
        password = findViewById(R.id.editTextPassword);

        forgetPassword = findViewById(R.id.forgetPasswordTextView);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerLinearLayout);
    }

    private void buttonClickListeners() {;
        registerButton.setOnClickListener(v -> {
            Intent registerIntent = new Intent(Login.this, Register1.class);
            startActivity(registerIntent);
        });

        forgetPassword.setOnClickListener(v -> {
            Intent forgetPasswordIntent = new Intent(Login.this, ResetPass1.class);
            startActivity(forgetPasswordIntent);
        });

        loginButton.setOnClickListener(v -> {
            Intent dashboardIntent = new Intent(Login.this, Dashboard.class);
            startActivity(dashboardIntent);
        });
    }
}