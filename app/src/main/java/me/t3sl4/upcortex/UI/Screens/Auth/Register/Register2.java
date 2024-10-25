package me.t3sl4.upcortex.UI.Screens.Auth.Register;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.zpj.widget.checkbox.ZCheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Utils.BaseUtil;
import me.t3sl4.upcortex.Utils.HTTP.Requests.Auth.AuthService;
import me.t3sl4.upcortex.Utils.Screen.ScreenUtil;
import me.t3sl4.upcortex.Utils.SharedPreferences.SharedPreferencesManager;

public class Register2 extends AppCompatActivity {

    private TextInputEditText addressName;
    private TextInputEditText name;
    private TextInputEditText surname;
    private CountryCodePicker countryCode;
    private TextInputEditText phoneNumber;
    private TextInputEditText city;
    private TextInputEditText district;
    private TextInputEditText neighborhood;
    private TextInputEditText zipCode;
    private TextInputEditText addressDetail;
    private ZCheckBox confirmAddress;
    private TextView termsViewer;
    private Button backButton;
    private Button nextButton;

    private JSONObject citiesJson;
    private JSONObject districtsJson;
    private JSONArray neighborhoodsJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);

        ScreenUtil.hideNavigationBar(this);

        initializeComponents();
        loadJsonData();
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
        zipCode = findViewById(R.id.editTextZipCode);
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
            if (!areAllFieldsFilled()) {
                Sneaker.with(Register2.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_fill_blanks))
                        .sneakError();
            } else if (confirmAddress.isChecked()) {
                saveData(); // Verileri kaydet

                String userName = SharedPreferencesManager.getSharedPref("name", this, "");
                String userSurname = SharedPreferencesManager.getSharedPref("surname", this, "");
                String userEmail = SharedPreferencesManager.getSharedPref("eMail", this, "");
                String birthDate = BaseUtil.getValidBirthDate(SharedPreferencesManager.getSharedPref("birthDate", this, ""));
                String userAddress = SharedPreferencesManager.getSharedPref("neighboorhood", this, "") + " " + SharedPreferencesManager.getSharedPref("addressDetail", this, "") + " " + SharedPreferencesManager.getSharedPref("zipCode", this, "") + " " + SharedPreferencesManager.getSharedPref("district", this, "") + " " + SharedPreferencesManager.getSharedPref("city", this, "");
                String userPassword = SharedPreferencesManager.getSharedPref("password", this, "");
                String dialCode = "+" + SharedPreferencesManager.getSharedPref("countryCode", this, "");
                String userPhone = dialCode + SharedPreferencesManager.getSharedPref("phoneNumber", this, "");
                String userIdentity = SharedPreferencesManager.getSharedPref("idNumber", this, "");

                AuthService.register(Register2.this,
                        userName, userSurname, userEmail, birthDate, userAddress, userPassword, dialCode, userPhone, userIdentity, () -> {
                            Intent intent = new Intent(Register2.this, Register3.class);
                            startActivity(intent);
                            finish();
                        }, () -> {
                            Sneaker.with(Register2.this)
                                    .setTitle(getString(R.string.error_title))
                                    .setMessage(getString(R.string.error_register_not_complete))
                                    .sneakError();
                        });
            } else {
                Sneaker.with(Register2.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_term_confirm))
                        .sneakError();
            }
        });

        city.setOnClickListener(v -> {
            hideKeyboard();
            showCityDialog();
        });

        city.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                hideKeyboard();
                showCityDialog();
            }
        });

        district.setOnClickListener(v -> {
            hideKeyboard();
            showDistrictDialog();
        });

        district.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                hideKeyboard();
                showDistrictDialog();
            }
        });

        neighborhood.setOnClickListener(v -> {
            hideKeyboard();
            showNeighborhoodDialog();
        });

        neighborhood.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                hideKeyboard();
                showNeighborhoodDialog();
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showCityDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_select_item);
        ListView listView = dialog.findViewById(R.id.list_view);

        List<String> cityNames = new ArrayList<>();
        Iterator<String> keys = citiesJson.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                cityNames.add(citiesJson.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Plakaya göre sıralama
        Collections.sort(cityNames, (city1, city2) -> {
            try {
                String key1 = getCityKeyByName(city1);
                String key2 = getCityKeyByName(city2);
                return Integer.compare(Integer.parseInt(key1), Integer.parseInt(key2));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return city1.compareTo(city2);
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCity = cityNames.get(position);
            city.setText(selectedCity);
            district.setText("");
            neighborhood.setText("");
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showDistrictDialog() {
        if (city.getText().toString().isEmpty()) {
            Sneaker.with(this)
                    .setTitle(getString(R.string.error_title))
                    .setMessage(getString(R.string.error_select_city_first))
                    .sneakError();
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_select_item);
        ListView listView = dialog.findViewById(R.id.list_view);

        String cityKey = getCityKeyByName(city.getText().toString());
        List<String> districtNames = new ArrayList<>();
        try {
            JSONArray districtsArray = districtsJson.getJSONArray(cityKey);
            for (int i = 0; i < districtsArray.length(); i++) {
                districtNames.add(districtsArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, districtNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDistrict = districtNames.get(position);
            district.setText(selectedDistrict);
            neighborhood.setText("");
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showNeighborhoodDialog() {
        if (city.getText().toString().isEmpty() || district.getText().toString().isEmpty()) {
            Sneaker.with(this)
                    .setTitle(getString(R.string.error_title))
                    .setMessage(getString(R.string.error_select_city_district_first))
                    .sneakError();
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_select_item);
        ListView listView = dialog.findViewById(R.id.list_view);

        String cityKey = getCityKeyByName(city.getText().toString());
        String districtName = district.getText().toString();
        List<String> neighborhoodNames = new ArrayList<>();
        for (int i = 0; i < neighborhoodsJson.length(); i++) {
            try {
                JSONArray neighborhoodArray = neighborhoodsJson.getJSONArray(i);
                if (neighborhoodArray.getString(0).equals(cityKey) && neighborhoodArray.getString(2).equals(districtName)) {
                    neighborhoodNames.add(neighborhoodArray.getString(3));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, neighborhoodNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedNeighborhood = neighborhoodNames.get(position);
            neighborhood.setText(selectedNeighborhood);
            dialog.dismiss();
        });

        dialog.show();
    }

    private String getCityKeyByName(String cityName) {
        Iterator<String> keys = citiesJson.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                if (citiesJson.getString(key).equals(cityName)) {
                    return key;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean areAllFieldsFilled() {
        return !addressName.getText().toString().isEmpty()
                && !name.getText().toString().isEmpty()
                && !surname.getText().toString().isEmpty()
                && !phoneNumber.getText().toString().isEmpty()
                && !city.getText().toString().isEmpty()
                && !district.getText().toString().isEmpty()
                && !neighborhood.getText().toString().isEmpty()
                && !zipCode.getText().toString().isEmpty()
                && !addressDetail.getText().toString().isEmpty();
    }

    private void saveData() {
        SharedPreferencesManager.writeSharedPref("addressName", addressName.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("name", name.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("surname", surname.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("countryCode", countryCode.getSelectedCountryCode(), this);
        SharedPreferencesManager.writeSharedPref("phoneNumber", phoneNumber.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("city", city.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("district", district.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("neighborhood", neighborhood.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("zipCode", zipCode.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("addressDetail", addressDetail.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("confirmAddress", confirmAddress.isChecked(), this);
    }

    private void loadSavedData() {
        String addressNameValue = SharedPreferencesManager.getSharedPref("addressName", this, "");
        if (!addressNameValue.isEmpty()) {
            addressName.setText(addressNameValue);
        }

        String nameValue = SharedPreferencesManager.getSharedPref("name", this, "");
        if (!nameValue.isEmpty()) {
            name.setText(nameValue);
        }

        String surnameValue = SharedPreferencesManager.getSharedPref("surname", this, "");
        if (!surnameValue.isEmpty()) {
            surname.setText(surnameValue);
        }

        String countryCodeValue = SharedPreferencesManager.getSharedPref("countryCode", this, "");
        if (!countryCodeValue.isEmpty()) {
            countryCode.setCountryForPhoneCode(Integer.parseInt(countryCodeValue));
        }

        String phoneNumberValue = SharedPreferencesManager.getSharedPref("phoneNumber", this, "");
        if (!phoneNumberValue.isEmpty()) {
            phoneNumber.setText(phoneNumberValue);
        }

        String cityValue = SharedPreferencesManager.getSharedPref("city", this, "");
        if (!cityValue.isEmpty()) {
            city.setText(cityValue);
            loadDistricts(cityValue);
        }

        String districtValue = SharedPreferencesManager.getSharedPref("district", this, "");
        if (!districtValue.isEmpty()) {
            district.setText(districtValue);
            loadNeighborhoods(cityValue, districtValue);
        }

        String neighborhoodValue = SharedPreferencesManager.getSharedPref("neighborhood", this, "");
        if (!neighborhoodValue.isEmpty()) {
            neighborhood.setText(neighborhoodValue);
        }

        String zipCodeValue = SharedPreferencesManager.getSharedPref("zipCode", this, "");
        if (!zipCodeValue.isEmpty()) {
            zipCode.setText(zipCodeValue);
        }

        String addressDetailValue = SharedPreferencesManager.getSharedPref("addressDetail", this, "");
        if (!addressDetailValue.isEmpty()) {
            addressDetail.setText(addressDetailValue);
        }

        boolean confirmAddressValue = SharedPreferencesManager.getSharedPref("confirmAddress", this, false);
        confirmAddress.setChecked(confirmAddressValue);
    }

    private void loadDistricts(String cityName) {
        String cityKey = getCityKeyByName(cityName);
        List<String> districtNames = new ArrayList<>();
        try {
            JSONArray districtsArray = districtsJson.getJSONArray(cityKey);
            for (int i = 0; i < districtsArray.length(); i++) {
                districtNames.add(districtsArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadNeighborhoods(String cityName, String districtName) {
        String cityKey = getCityKeyByName(cityName);
        List<String> neighborhoodNames = new ArrayList<>();
        for (int i = 0; i < neighborhoodsJson.length(); i++) {
            try {
                JSONArray neighborhoodArray = neighborhoodsJson.getJSONArray(i);
                if (neighborhoodArray.getString(0).equals(cityKey) && neighborhoodArray.getString(2).equals(districtName)) {
                    neighborhoodNames.add(neighborhoodArray.getString(3));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadJsonData() {
        try {
            citiesJson = new JSONObject(loadJSONFromAsset("city.json"));
            districtsJson = new JSONObject(loadJSONFromAsset("district.json"));
            neighborhoodsJson = new JSONArray(loadJSONFromAsset("neighborhood.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAsset(String fileName) {
        String json;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}