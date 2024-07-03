package me.t3sl4.upcortex.UI.Screens.Auth.Register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.zpj.widget.checkbox.ZCheckBox;

import java.util.Calendar;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.DatePicker.DatePickerBottomSheet;
import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;

public class Register1 extends AppCompatActivity {

    private TextInputEditText nameEditText;
    private TextInputEditText surnameEditText;
    private TextInputEditText idNumberEditText;
    private TextInputEditText birthDateEditText;
    private CountryCodePicker countryCodePicker;
    private TextInputEditText phoneNumberEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText repeatPasswordEditText;
    private ZCheckBox termsAndConditionsCheckBox;
    private TextView termsViewerTextView;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_1);

        NavigationBarUtil.hideNavigationBar(this);

        initializeComponents();
        buttonClickListeners();
    }

    private void initializeComponents() {
        nameEditText = findViewById(R.id.editTextName);
        surnameEditText = findViewById(R.id.editTextSurname);
        idNumberEditText = findViewById(R.id.editTextIDNumber);
        birthDateEditText = findViewById(R.id.editTextBirthDate);
        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
        passwordEditText = findViewById(R.id.editTextPassword);
        repeatPasswordEditText = findViewById(R.id.editTextPasswordRepeat);
        termsAndConditionsCheckBox = findViewById(R.id.termsCheckBox);
        termsViewerTextView = findViewById(R.id.termsViewText);
        nextButton = findViewById(R.id.nextButton);
    }

    private void buttonClickListeners() {
        nextButton.setOnClickListener(v -> {
            if(termsAndConditionsCheckBox.isChecked()) {
                Intent intent = new Intent(Register1.this, Register2.class);
                startActivity(intent);
                finish();
            } else {
                Sneaker.with(Register1.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_term_confirm))
                        .sneakError();
            }
        });

        birthDateEditText.setOnClickListener(v -> {
            hideKeyboard();
            showDatePicker();
        });

        birthDateEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                hideKeyboard();
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        DatePickerBottomSheet datePickerBottomSheet = new DatePickerBottomSheet(this, (day, month, year) -> {
            String selectedDate = day + "/" + (month + 1) + "/" + year;
            birthDateEditText.setText(selectedDate); // Seçilen tarihi EditText'e ekle
        });
        datePickerBottomSheet.show();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean isAgeValid() {
        String birthDateString = birthDateEditText.getText().toString();
        if (birthDateString.isEmpty()) {
            return false;
        }

        String[] parts = birthDateString.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]) - 1;
        int year = Integer.parseInt(parts[2]);

        Calendar birthDate = Calendar.getInstance();
        birthDate.set(year, month, day);

        Calendar today = Calendar.getInstance();
        today.add(Calendar.YEAR, -12);

        return !today.before(birthDate);
    }
}
