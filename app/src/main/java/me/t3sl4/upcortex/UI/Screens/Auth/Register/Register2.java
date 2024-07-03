package me.t3sl4.upcortex.UI.Screens.Auth.Register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.zpj.widget.checkbox.ZCheckBox;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Util.SharedPreferences.SPUtil;

public class Register2 extends AppCompatActivity {

    private TextInputEditText addressName;
    private TextInputEditText name;
    private TextInputEditText surname;
    private CountryCodePicker countryCode;
    private TextInputEditText phoneNumber;
    private TextInputEditText city;
    private TextInputEditText district;
    private TextInputEditText neighborhood;
    private TextInputEditText addressDetail;
    private ZCheckBox confirmAddress;
    private TextView termsViewer;
    private Button backButton;
    private Button nextButton;

    private SPUtil sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);
        NavigationBarUtil.hideNavigationBar(this);

        sharedPrefManager = new SPUtil(this);

        initializeComponents();
        loadSavedData(); // Kaydedilen verileri yükle
        buttonClickListeners();
    }

    private void initializeComponents() {
        addressName = findViewById(R.id.editTextAddresName);
        name = findViewById(R.id.editTextName);
        surname = findViewById(R.id.editTextSurname);
        countryCode = findViewById(R.id.country_code_picker);
        phoneNumber = findViewById(R.id.editTextPhoneNumber);
        city = findViewById(R.id.editTextCity);
        district = findViewById(R.id.editTextDistrict);
        neighborhood = findViewById(R.id.editTextNeighborhood);
        addressDetail = findViewById(R.id.editTextAddresDetail);
        confirmAddress = findViewById(R.id.confirmCheckBox);
        termsViewer = findViewById(R.id.termsViewText);
        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
    }

    private void buttonClickListeners() {
        backButton.setOnClickListener(v -> {
            finish();
        });

        nextButton.setOnClickListener(v -> {
            if (confirmAddress.isChecked()) {
                saveData(); // Verileri kaydet
                Intent intent = new Intent(Register2.this, Register3.class);
                startActivity(intent);
                finish();
            } else {
                Sneaker.with(Register2.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_term_confirm))
                        .sneakError();
            }
        });
    }

    private void saveData() {
        sharedPrefManager.saveString("addressName", addressName.getText().toString());
        sharedPrefManager.saveString("name", name.getText().toString());
        sharedPrefManager.saveString("surname", surname.getText().toString());
        sharedPrefManager.saveString("countryCode", countryCode.getSelectedCountryCode());
        sharedPrefManager.saveString("phoneNumber", phoneNumber.getText().toString());
        sharedPrefManager.saveString("city", city.getText().toString());
        sharedPrefManager.saveString("district", district.getText().toString());
        sharedPrefManager.saveString("neighborhood", neighborhood.getText().toString());
        sharedPrefManager.saveString("addressDetail", addressDetail.getText().toString());
        sharedPrefManager.saveBoolean("confirmAddress", confirmAddress.isChecked());
    }

    private void loadSavedData() {
        String addressNameValue = sharedPrefManager.getString("addressName");
        if (addressNameValue != null && !addressNameValue.isEmpty()) {
            addressName.setText(addressNameValue);
        }

        String nameValue = sharedPrefManager.getString("addressName_name");
        if (nameValue != null && !nameValue.isEmpty()) {
            name.setText(nameValue);
        }

        String surnameValue = sharedPrefManager.getString("addressName_surname");
        if (surnameValue != null && !surnameValue.isEmpty()) {
            surname.setText(surnameValue);
        }

        String countryCodeValue = sharedPrefManager.getString("addressName_countryCode");
        if (countryCodeValue != null && !countryCodeValue.isEmpty()) {
            countryCode.setCountryForPhoneCode(Integer.parseInt(countryCodeValue));
        }

        String phoneNumberValue = sharedPrefManager.getString("addressName_phoneNumber");
        if (phoneNumberValue != null && !phoneNumberValue.isEmpty()) {
            phoneNumber.setText(phoneNumberValue);
        }

        String cityValue = sharedPrefManager.getString("city");
        if (cityValue != null && !cityValue.isEmpty()) {
            city.setText(cityValue);
        }

        String districtValue = sharedPrefManager.getString("district");
        if (districtValue != null && !districtValue.isEmpty()) {
            district.setText(districtValue);
        }

        String neighborhoodValue = sharedPrefManager.getString("neighborhood");
        if (neighborhoodValue != null && !neighborhoodValue.isEmpty()) {
            neighborhood.setText(neighborhoodValue);
        }

        String addressDetailValue = sharedPrefManager.getString("addressDetail");
        if (addressDetailValue != null && !addressDetailValue.isEmpty()) {
            addressDetail.setText(addressDetailValue);
        }

        boolean confirmAddressValue = sharedPrefManager.getBoolean("confirmAddress");
        confirmAddress.setChecked(confirmAddressValue);
    }
}