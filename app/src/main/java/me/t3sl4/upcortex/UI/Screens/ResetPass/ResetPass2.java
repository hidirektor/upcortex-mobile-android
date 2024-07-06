package me.t3sl4.upcortex.UI.Screens.ResetPass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;

import me.t3sl4.upcortex.R;

public class ResetPass2 extends AppCompatActivity {

    private PinView enteredOTPCode;

    private Button backButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_2);

        initializeComponents();
        buttonClickListeners();
    }

    private void initializeComponents() {
        enteredOTPCode = findViewById(R.id.otpCodePinView);

        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
    }

    private void buttonClickListeners() {
        backButton.setOnClickListener(v -> {
            finish();
        });

        nextButton.setOnClickListener(v -> {
            Intent enterPassIntent = new Intent(this, ResetPass3.class);
            startActivity(enterPassIntent);
        });
    }
}
