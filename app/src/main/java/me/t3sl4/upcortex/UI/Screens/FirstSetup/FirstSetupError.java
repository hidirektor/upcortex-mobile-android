package me.t3sl4.upcortex.UI.Screens.FirstSetup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;

public class FirstSetupError extends AppCompatActivity {

    private Button backToMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup_error);

        initializeComponents();
        buttonClickListener();
    }

    private void initializeComponents() {
        backToMainButton = findViewById(R.id.backToMainButton);
    }

    private void buttonClickListener() {
        backToMainButton.setOnClickListener(v -> {
            Intent firstSetupIntent = new Intent(FirstSetupError.this, FirstSetup.class);
            startActivity(firstSetupIntent);
            finish();
        });
    }
}
