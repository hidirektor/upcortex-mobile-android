package me.t3sl4.upcortex.UI.Screens.ResetPass;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.Util.Utils;
import me.t3sl4.upcortex.Util.Web.WebUtil;

public class ResetPass1 extends AppCompatActivity {

    private CountryCodePicker countryCodePicker;
    private TextInputEditText phoneNumber;
    private TextView contactSupport;

    private Button backButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_1);

        initializeComponents();
        buttonClickListeners();
    }

    private void initializeComponents() {
        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumber = findViewById(R.id.editTextPhoneNumber);
        contactSupport = findViewById(R.id.contactSupport);

        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
    }

    private void buttonClickListeners() {
        backButton.setOnClickListener(v -> {
            finish();
        });

        contactSupport.setOnClickListener(v -> {
            WebUtil.openURL(this, Utils.SUPPORT_URL);
        });
    }
}
