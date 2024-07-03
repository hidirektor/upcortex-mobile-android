package me.t3sl4.upcortex.UI.Screens.Auth.Register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.zpj.widget.checkbox.ZCheckBox;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.SplashActivity;
import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;
import me.t3sl4.upcortex.UI.Screens.Auth.AuthSelection;
import me.t3sl4.upcortex.UI.Screens.OnBoard.OnBoard1;
import me.t3sl4.upcortex.Util.Utils;

public class Register1 extends AppCompatActivity {

    private TextInputEditText nameEditText;
    private TextInputEditText surnameEditText;
    private TextInputEditText idNumberEditText;
    private TextInputEditText birthDateEditText;
    private Spinner countryCodeSpinner;
    private TextInputEditText phoneNumberEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText repeatPasswordEditText;
    private ZCheckBox termsAndConditionsCheckBox;
    private TextView termsViewerTextView;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        NavigationBarUtil.hideNavigationBar(this);

        initializeComponents();
    }

    private void initializeComponents() {
        nameEditText = findViewById(R.id.editTextName);
        surnameEditText = findViewById(R.id.editTextSurname);
        idNumberEditText = findViewById(R.id.editTextIDNumber);
        birthDateEditText = findViewById(R.id.editTextBirthDate);
        countryCodeSpinner = findViewById(R.id.country_code_spinner);
        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
        passwordEditText = findViewById(R.id.editTextPassword);
        repeatPasswordEditText = findViewById(R.id.editTextPasswordRepeat);
        termsAndConditionsCheckBox = findViewById(R.id.termsCheckBox);
        termsViewerTextView = findViewById(R.id.termsViewText);
        nextButton = findViewById(R.id.nextButton);
    }
}
