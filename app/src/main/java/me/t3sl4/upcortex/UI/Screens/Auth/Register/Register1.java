package me.t3sl4.upcortex.UI.Screens.Auth.Register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.zpj.widget.checkbox.ZCheckBox;

import java.util.Calendar;
import java.util.regex.Pattern;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.DatePicker.DatePickerBottomSheet;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Utils.BaseUtil;
import me.t3sl4.upcortex.Utils.HTTP.Requests.Auth.AuthService;
import me.t3sl4.upcortex.Utils.Screen.ScreenListeners;
import me.t3sl4.upcortex.Utils.Screen.ScreenUtil;
import me.t3sl4.upcortex.Utils.SharedPreferences.SharedPreferencesManager;

public class Register1 extends AppCompatActivity {

    private TextInputEditText nameEditText;
    private TextInputEditText surnameEditText;
    private TextInputEditText idNumberEditText;
    private TextInputEditText birthDateEditText;
    private CountryCodePicker countryCodePicker;
    private TextInputEditText phoneNumberEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText repeatPasswordEditText;
    private ZCheckBox termsAndConditionsCheckBox;
    private ZCheckBox noEmailCheckBox;
    private TextView termsViewerTextView;
    private Button nextButton;

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String TURKISH_PHONE_NUMBER_PATTERN = "^5\\d{2} \\d{3} \\d{2} \\d{2}$";

    Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    Pattern turkishPhoneNumberPattern = Pattern.compile(TURKISH_PHONE_NUMBER_PATTERN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_1);

        ScreenUtil.hideNavigationBar(this);

        initializeComponents();
        loadSavedData(); // Kaydedilen verileri yükle
        buttonClickListeners();
        ScreenListeners.passwordListener(passwordEditText);
        ScreenListeners.passwordListener(repeatPasswordEditText);
    }

    private void initializeComponents() {
        nameEditText = findViewById(R.id.editTextName);
        surnameEditText = findViewById(R.id.editTextSurname);
        idNumberEditText = findViewById(R.id.editTextIDNumber);
        idNumberEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
        idNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 11) {
                    idNumberEditText.setError(getString(R.string.error_invalid_id_number));
                } else {
                    idNumberEditText.setError(null);
                }
            }
        });
        birthDateEditText = findViewById(R.id.editTextBirthDate);
        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;

                if (countryCodePicker.getSelectedCountryCode().equals("90")) {
                    String input = s.toString().replaceAll("\\s+", "");

                    if (input.length() <= 10 && input.startsWith("5")) {
                        isFormatting = true;

                        StringBuilder formatted = new StringBuilder(input);
                        if (input.length() >= 3) formatted.insert(3, " ");
                        if (input.length() >= 6) formatted.insert(7, " ");
                        if (input.length() >= 8) formatted.insert(10, " ");

                        phoneNumberEditText.setText(formatted.toString());
                        phoneNumberEditText.setSelection(formatted.length());

                        isFormatting = false;
                    }

                    if (!turkishPhoneNumberPattern.matcher(phoneNumberEditText.getText().toString()).matches()) {
                        phoneNumberEditText.setError(getString(R.string.error_turkish_number_format));
                    } else {
                        phoneNumberEditText.setError(null);
                    }
                }
            }
        });
        emailEditText = findViewById(R.id.editTextEmail);
        emailEditText.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;

                String input = s.toString();

                isFormatting = true;

                if (!emailPattern.matcher(input).matches()) {
                    emailEditText.setError(getString(R.string.error_invalid_email));
                } else {
                    emailEditText.setError(null);
                }

                isFormatting = false;
            }
        });
        passwordEditText = findViewById(R.id.editTextPassword);
        repeatPasswordEditText = findViewById(R.id.editTextPasswordRepeat);
        termsAndConditionsCheckBox = findViewById(R.id.termsCheckBox);
        noEmailCheckBox = findViewById(R.id.noEmailCheckbox);
        termsViewerTextView = findViewById(R.id.termsViewText);
        nextButton = findViewById(R.id.nextButton);

        noEmailCheckBox.setOnCheckedChangeListener((checkBox, isChecked) -> {
            if(isChecked) {
                emailEditText.setText("Default");
                emailEditText.setEnabled(false);
            } else {
                emailEditText.setText("");
                emailEditText.setEnabled(true);
            }
        });
    }

    private void buttonClickListeners() {
        nextButton.setOnClickListener(v -> {
            if (!areAllFieldsFilled()) {
                Sneaker.with(Register1.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_fill_blanks))
                        .sneakError();
            } else if (!isAgeValid()) {
                Sneaker.with(Register1.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_age_error))
                        .sneakError();
            } else if (!termsAndConditionsCheckBox.isChecked()) {
                Sneaker.with(Register1.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_term_confirm))
                        .sneakError();
            } else if(idNumberEditText.getError() != null) {
                Sneaker.with(Register1.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_register_format))
                        .sneakError();
            } else if(phoneNumberEditText.getError() != null) {
                Sneaker.with(Register1.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_register_format))
                        .sneakError();
            } else if(emailEditText.getError() != null) {
                Sneaker.with(Register1.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_register_format))
                        .sneakError();
            } else {
                saveData(); // Verileri kaydet

                String userName = SharedPreferencesManager.getSharedPref("name", this, "");
                String userSurname = SharedPreferencesManager.getSharedPref("surname", this, "");
                String userEmail = SharedPreferencesManager.getSharedPref("eMail", this, "");
                String birthDate = BaseUtil.getValidBirthDate(SharedPreferencesManager.getSharedPref("birthDate", this, ""));
                String userAddress = SharedPreferencesManager.getSharedPref("neighborhood", this, "") + " " + SharedPreferencesManager.getSharedPref("addressDetail", this, "") + " " + SharedPreferencesManager.getSharedPref("zipCode", this, "") + " " + SharedPreferencesManager.getSharedPref("district", this, "") + " " + SharedPreferencesManager.getSharedPref("city", this, "");
                String userPassword = SharedPreferencesManager.getSharedPref("password", this, "");
                String dialCode = "+" + SharedPreferencesManager.getSharedPref("countryCode", this, "");
                String userPhone = SharedPreferencesManager.getSharedPref("phoneNumber", this, "").replace(" ", "");
                String userIdentity = SharedPreferencesManager.getSharedPref("idNumber", this, "");

                AuthService.register(Register1.this,
                        userName, userSurname, userEmail, birthDate, userAddress, userPassword, dialCode, userPhone, userIdentity, () -> {
                            AuthService.login(Register1.this, userIdentity, userPassword, () -> {
                                Intent intent = new Intent(Register1.this, Register2.class);
                                startActivity(intent);
                                finish();
                            }, () -> {
                                Sneaker.with(Register1.this)
                                        .setTitle(getString(R.string.error_title))
                                        .setMessage(getString(R.string.error_register_not_complete))
                                        .sneakError();
                            });
                        }, () -> {
                            Sneaker.with(Register1.this)
                                    .setTitle(getString(R.string.error_title))
                                    .setMessage(getString(R.string.error_register_not_complete))
                                    .sneakError();
                        });
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

    private void saveData() {
        SharedPreferencesManager.writeSharedPref("name", nameEditText.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("surname", surnameEditText.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("idNumber", idNumberEditText.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("birthDate", birthDateEditText.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("countryCode", countryCodePicker.getSelectedCountryCode(), this);
        SharedPreferencesManager.writeSharedPref("phoneNumber", phoneNumberEditText.getText().toString(), this);
        if(emailEditText.getText().equals("Default")) {
            SharedPreferencesManager.writeSharedPref("eMail", "default-mail@dinamikbeyin.com", this);
        } else {
            SharedPreferencesManager.writeSharedPref("eMail", emailEditText.getText().toString(), this);
        }
        SharedPreferencesManager.writeSharedPref("password", passwordEditText.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("repeatPassword", repeatPasswordEditText.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("termsAccepted", termsAndConditionsCheckBox.isChecked(), this);
    }

    private void loadSavedData() {
        String name = SharedPreferencesManager.getSharedPref("name", this, "");
        if (!name.isEmpty()) {
            nameEditText.setText(name);
        }

        String surname = SharedPreferencesManager.getSharedPref("surname", this, "");
        if (!surname.isEmpty()) {
            surnameEditText.setText(surname);
        }

        String idNumber = SharedPreferencesManager.getSharedPref("idNumber", this, "");
        if (!idNumber.isEmpty()) {
            idNumberEditText.setText(idNumber);
        }

        String birthDate = SharedPreferencesManager.getSharedPref("birthDate", this, "");
        if (!birthDate.isEmpty()) {
            birthDateEditText.setText(birthDate);
        }

        String countryCode = SharedPreferencesManager.getSharedPref("countryCode", this, "");
        if (!countryCode.isEmpty()) {
            countryCodePicker.setCountryForPhoneCode(Integer.parseInt(countryCode));
        }

        String phoneNumber = SharedPreferencesManager.getSharedPref("phoneNumber", this, "");
        if (!phoneNumber.isEmpty()) {
            phoneNumberEditText.setText(phoneNumber);
        }

        String eMail = SharedPreferencesManager.getSharedPref("eMail", this, "");
        if (!eMail.isEmpty()) {
            emailEditText.setText(eMail);
        }

        String password = SharedPreferencesManager.getSharedPref("password", this, "");
        if (!password.isEmpty()) {
            passwordEditText.setText(password);
        }

        String repeatPassword = SharedPreferencesManager.getSharedPref("repeatPassword", this, "");
        if (!repeatPassword.isEmpty()) {
            repeatPasswordEditText.setText(repeatPassword);
        }

        boolean termsAccepted = SharedPreferencesManager.getSharedPref("termsAccepted", this, false);
        termsAndConditionsCheckBox.setChecked(termsAccepted);
    }

    private boolean areAllFieldsFilled() {
        return !nameEditText.getText().toString().isEmpty()
                && !surnameEditText.getText().toString().isEmpty()
                && !idNumberEditText.getText().toString().isEmpty()
                && !birthDateEditText.getText().toString().isEmpty()
                && !phoneNumberEditText.getText().toString().isEmpty()
                && !passwordEditText.getText().toString().isEmpty()
                && !repeatPasswordEditText.getText().toString().isEmpty();
    }
}