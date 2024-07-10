package me.t3sl4.upcortex.UI.Screens.FirstSetup;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Screens.Auth.AuthSelection;
import me.t3sl4.upcortex.Util.SharedPreferences.SPUtil;
import me.t3sl4.upcortex.Util.Utils;

public class FirstSetup extends AppCompatActivity {

    private Button ageYesButton;
    private Button ageNoButton;

    private Button heartYesButton;
    private Button heartNoButton;

    private Button brainYesButton;
    private Button brainNoButton;

    private Button nextButton;

    boolean ageStatus;
    boolean heartStatus;
    boolean brainStatus;

    private SPUtil sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup);

        Utils.hideNavigationBar(this);
        Utils.hideStatusBar(this);

        sharedPrefManager = new SPUtil(this);

        initializeComponents();
        buttonClickListener();
    }

    private void initializeComponents() {
        ageYesButton = findViewById(R.id.ageYesButton);
        ageNoButton = findViewById(R.id.ageNoButton);

        heartYesButton = findViewById(R.id.heartYesButton);
        heartNoButton = findViewById(R.id.heartNoButton);

        brainYesButton = findViewById(R.id.brainYesButton);
        brainNoButton = findViewById(R.id.brainNoButton);

        nextButton = findViewById(R.id.nextButton);

        resetButtons();
    }

    private void buttonClickListener() {
        nextButton.setOnClickListener(v -> {
            Intent redirectIntent;
            if(checkStatus()) {
                sharedPrefManager.saveBoolean("canAccess", true);
                redirectIntent = new Intent(FirstSetup.this, AuthSelection.class);
            } else {
                sharedPrefManager.saveBoolean("canAccess", false);
                redirectIntent = new Intent(FirstSetup.this, FirstSetupError.class);
            }
            startActivity(redirectIntent);
            finish();
        });

        ageYesButton.setOnClickListener(v -> {
            buttonStatusSwitch(ageYesButton, true);
            buttonStatusSwitch(ageNoButton, false);
            ageStatus = true;
        });

        heartYesButton.setOnClickListener(v -> {
            buttonStatusSwitch(heartYesButton, true);
            buttonStatusSwitch(heartNoButton, false);
            heartStatus = true;
        });

        brainYesButton.setOnClickListener(v -> {
            buttonStatusSwitch(brainYesButton, true);
            buttonStatusSwitch(brainNoButton, false);
            brainStatus = true;
        });

        ageNoButton.setOnClickListener(v -> {
            buttonStatusSwitch(ageNoButton, true);
            buttonStatusSwitch(ageYesButton, false);
            ageStatus = false;
        });

        heartNoButton.setOnClickListener(v -> {
            buttonStatusSwitch(heartNoButton, true);
            buttonStatusSwitch(heartYesButton, false);
            heartStatus = false;
        });

        brainNoButton.setOnClickListener(v -> {
            buttonStatusSwitch(brainNoButton, true);
            buttonStatusSwitch(brainYesButton, false);
            brainStatus = false;
        });
    }

    private boolean checkStatus() {
        if(ageStatus && !heartStatus && !brainStatus) {
            return true;
        }

        return false;
    }

    private void resetButtons() {
        buttonStatusSwitch(ageYesButton, false);
        buttonStatusSwitch(heartYesButton, false);
        buttonStatusSwitch(brainYesButton, false);
    }

    private void buttonStatusSwitch(Button inputButton, boolean buttonStatus) {
        int defaultTextColor = ContextCompat.getColor(this, R.color.darkBaseColor);
        int secondTextColor = ContextCompat.getColor(this, R.color.white);

        Drawable defaultDrawable = ContextCompat.getDrawable(this, R.drawable.button_fs_no);
        Drawable secondDrawable = ContextCompat.getDrawable(this, R.drawable.button_fs_yes);

        if(buttonStatus) {
            inputButton.setTextColor(secondTextColor);
            inputButton.setBackground(secondDrawable);
        } else {
            inputButton.setTextColor(defaultTextColor);
            inputButton.setBackground(defaultDrawable);
        }
    }
}
