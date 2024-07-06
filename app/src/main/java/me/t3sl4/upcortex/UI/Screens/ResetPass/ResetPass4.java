package me.t3sl4.upcortex.UI.Screens.ResetPass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Screens.Auth.Login;

public class ResetPass4 extends AppCompatActivity {

    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_3);

        initializeComponents();
        buttonClickListeners();
    }

    private void initializeComponents() {
        nextButton = findViewById(R.id.nextButton);
    }

    private void buttonClickListeners() {
        nextButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(this, Login.class);
            startActivity(loginIntent);
            finish();
        });
    }
}
