package me.t3sl4.upcortex.UI.Screens.ResetPass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import me.t3sl4.upcortex.R;

public class ResetPass3 extends AppCompatActivity {

    private TextInputEditText passwordText;
    private TextInputEditText repeatPasswordText;

    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_3);

        initializeComponents();
        buttonClickListeners();
    }

    private void initializeComponents() {
        passwordText = findViewById(R.id.editTextPassword);
        repeatPasswordText = findViewById(R.id.editTextPasswordRepeat);

        nextButton = findViewById(R.id.nextButton);
    }

    private void buttonClickListeners() {
        nextButton.setOnClickListener(v -> {
            Intent enterPassIntent = new Intent(this, ResetPass4.class);
            startActivity(enterPassIntent);
        });
    }
}
