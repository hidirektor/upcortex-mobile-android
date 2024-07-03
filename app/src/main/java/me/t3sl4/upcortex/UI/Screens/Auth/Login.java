package me.t3sl4.upcortex.UI.Screens.Auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;

public class Login extends AppCompatActivity {

    private TextInputEditText idNumber;
    private TextInputEditText password;

    private TextView forgetPassword;
    private Button loginButton;
    private LinearLayout registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_selection);

        NavigationBarUtil.hideNavigationBar(this);

        initializeComponents();
    }

    private void initializeComponents() {
        idNumber = findViewById(R.id.editTextIDNumber);
        password = findViewById(R.id.editTextPassword);

        forgetPassword = findViewById(R.id.forgetPasswordTextView);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerLinearLayout);
    }
}