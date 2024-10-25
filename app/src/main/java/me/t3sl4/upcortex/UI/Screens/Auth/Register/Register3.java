package me.t3sl4.upcortex.UI.Screens.Auth.Register;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.zpj.widget.checkbox.ZCheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.DatePicker.ExpiryDatePicker;
import me.t3sl4.upcortex.UI.Components.PaymentOptions.PaymentOptions;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Utils.BaseUtil;
import me.t3sl4.upcortex.Utils.HTTP.Requests.Payment.IyzicoService;
import me.t3sl4.upcortex.Utils.Screen.ScreenUtil;
import me.t3sl4.upcortex.Utils.SharedPreferences.SharedPreferencesManager;

public class Register3 extends AppCompatActivity implements ExpiryDatePicker.ExpiryDateSelectedListener  {

    private Button monthlyButton;
    private Button sixMonthButton;
    private Button annuallyButton;

    private TextView packagePrice;
    private Button packagePromotion;
    private TextView package_spec_1;
    private TextView package_spec_2;
    private TextView package_spec_3;

    private ZCheckBox confirmCheckBox;
    private TextView termsViewText;

    private Button backButton;
    private Button nextButton;

    private MaterialCardView packageCard;
    private MaterialCardView packageSummaryCard;

    private Button buttonSummary;
    private TextView packagePriceSummary;

    private LinearLayout creditCardLayout;
    private TextInputEditText editTextCardNumber;
    private TextInputEditText editTextHolderName;
    private TextInputEditText editTextExpiryDate;
    private TextInputEditText editTextCVV;
    private LinearLayout paymentOptionsButton;

    private String uniqueID;
    private String packageName;
    private String selectedPaymentOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_3);

        ScreenUtil.hideNavigationBar(this);

        initializeComponents();
        loadSavedData(); // Kaydedilen verileri yükle
        buttonClickListeners();
        summarySection();

        uniqueID = UUID.randomUUID().toString();
    }

    private void initializeComponents() {
        monthlyButton = findViewById(R.id.buttonMonthly);
        sixMonthButton = findViewById(R.id.button6Month);
        annuallyButton = findViewById(R.id.buttonAnnually);

        packagePrice = findViewById(R.id.packagePrice);
        packagePromotion = findViewById(R.id.promotionButton);
        package_spec_1 = findViewById(R.id.package_spec_1);
        package_spec_2 = findViewById(R.id.package_spec_2);
        package_spec_3 = findViewById(R.id.package_spec_3);

        confirmCheckBox = findViewById(R.id.confirmCheckBox);
        termsViewText = findViewById(R.id.termsViewText);

        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);

        packageCard = findViewById(R.id.subscriptionCard);
        packageSummaryCard = findViewById(R.id.subscriptionSummaryCard);

        buttonSummary = findViewById(R.id.buttonSummary);
        packagePriceSummary = findViewById(R.id.packagePriceSummary);

        creditCardLayout = findViewById(R.id.creditCardLayout);
        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextHolderName = findViewById(R.id.editTextHolderName);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        editTextCVV = findViewById(R.id.editTextCVV);
        paymentOptionsButton = findViewById(R.id.paymentOptionsButton);
    }

    private void buttonClickListeners() {
        backButton.setOnClickListener(v -> {
            finish();
        });

        nextButton.setOnClickListener(v -> {
            if (!areAllFieldsFilled()) {
                Sneaker.with(Register3.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_fill_blanks))
                        .sneakError();
            } else if (confirmCheckBox.isChecked()) {
                saveData(); // Verileri kaydet

                JSONObject buyer = new JSONObject();
                try {
                    buyer.put("id", "123");
                    buyer.put("name", SharedPreferencesManager.getSharedPref("name", this, ""));
                    buyer.put("surname", SharedPreferencesManager.getSharedPref("surname", this, ""));
                    buyer.put("identityNumber", SharedPreferencesManager.getSharedPref("idNumber", this, ""));
                    buyer.put("email", "info@hidirektor.com.tr");
                    buyer.put("gsmNumber", SharedPreferencesManager.getSharedPref("phoneNumber", this, ""));
                    buyer.put("registrationDate", BaseUtil.getCurrentDateTime());
                    buyer.put("lastLoginDate", BaseUtil.getCurrentDateTime());
                    buyer.put("registrationAddress", SharedPreferencesManager.getSharedPref("neighborhood", this, "") + " " + SharedPreferencesManager.getSharedPref("addressDetail", this, ""));
                    buyer.put("city", SharedPreferencesManager.getSharedPref("city", this, ""));
                    buyer.put("country", SharedPreferencesManager.getSharedPref("country", this, ""));
                    buyer.put("zipCode", SharedPreferencesManager.getSharedPref("countryCode", this, ""));
                    buyer.put("ip", BaseUtil.getUserIp(Register3.this));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                JSONObject userAddress = new JSONObject();
                try {
                    userAddress.put("address", SharedPreferencesManager.getSharedPref("neighborhood", this, "") + " " + SharedPreferencesManager.getSharedPref("addressDetail", this, ""));
                    userAddress.put("zipCode", SharedPreferencesManager.getSharedPref("countryCode", this, ""));
                    userAddress.put("contactName", SharedPreferencesManager.getSharedPref("name", this, "") + " " + SharedPreferencesManager.getSharedPref("surname", this, ""));
                    userAddress.put("city", SharedPreferencesManager.getSharedPref("city", this, ""));
                    userAddress.put("country", SharedPreferencesManager.getSharedPref("district", this, ""));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                JSONArray productArray = new JSONArray();

                JSONObject singleProduct = new JSONObject();
                try {
                    singleProduct.put("id", "1");
                    singleProduct.put("name", "Alzheimer Paketi 3 Aylık");
                    singleProduct.put("price", "300.0");
                    singleProduct.put("category1", "Alzheimer");
                    singleProduct.put("category2", "3 Aylık");
                    singleProduct.put("itemType", "VIRTUAL");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                productArray.put(singleProduct);

                JSONArray taksitListesi = new JSONArray();
                taksitListesi.put(1);
                taksitListesi.put(2);
                taksitListesi.put(4);
                taksitListesi.put(6);
                taksitListesi.put(9);
                taksitListesi.put(12);

                String jsonBody = "{\n" +
                        "    \"locale\": \"tr\",\n" +
                        "    \"conversationId\": \"123456789\",\n" +
                        "    \"price\": \"1.0\",\n" +
                        "    \"basketId\": \"B67832\",\n" +
                        "    \"paymentGroup\": \"PRODUCT\",\n" +
                        "    \"buyer\": {\n" +
                        "        \"id\": \"BY789\",\n" +
                        "        \"name\": \"John\",\n" +
                        "        \"surname\": \"Doe\",\n" +
                        "        \"identityNumber\": \"74300864791\",\n" +
                        "        \"email\": \"email@email.com\",\n" +
                        "        \"gsmNumber\": \"+905350000000\",\n" +
                        "        \"registrationDate\": \"2013-04-21 15:12:09\",\n" +
                        "        \"lastLoginDate\": \"2015-10-05 12:43:35\",\n" +
                        "        \"registrationAddress\": \"Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1\",\n" +
                        "        \"city\": \"Istanbul\",\n" +
                        "        \"country\": \"Turkey\",\n" +
                        "        \"zipCode\": \"34732\",\n" +
                        "        \"ip\": \"85.34.78.112\"\n" +
                        "    },\n" +
                        "    \"shippingAddress\": {\n" +
                        "        \"address\": \"Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1\",\n" +
                        "        \"zipCode\": \"34742\",\n" +
                        "        \"contactName\": \"Jane Doe\",\n" +
                        "        \"city\": \"Istanbul\",\n" +
                        "        \"country\": \"Turkey\"\n" +
                        "    },\n" +
                        "    \"billingAddress\": {\n" +
                        "        \"address\": \"Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1\",\n" +
                        "        \"zipCode\": \"34742\",\n" +
                        "        \"contactName\": \"Jane Doe\",\n" +
                        "        \"city\": \"Istanbul\",\n" +
                        "        \"country\": \"Turkey\"\n" +
                        "    },\n" +
                        "    \"basketItems\": [\n" +
                        "        {\n" +
                        "            \"id\": \"BI101\",\n" +
                        "            \"price\": \"0.3\",\n" +
                        "            \"name\": \"Binocular\",\n" +
                        "            \"category1\": \"Collectibles\",\n" +
                        "            \"category2\": \"Accessories\",\n" +
                        "            \"itemType\": \"PHYSICAL\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"id\": \"BI102\",\n" +
                        "            \"price\": \"0.5\",\n" +
                        "            \"name\": \"Game code\",\n" +
                        "            \"category1\": \"Game\",\n" +
                        "            \"category2\": \"Online Game Items\",\n" +
                        "            \"itemType\": \"VIRTUAL\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"id\": \"BI103\",\n" +
                        "            \"name\": \"Usb\",\n" +
                        "            \"price\": \"0.2\",\n" +
                        "            \"category1\": \"Electronics\",\n" +
                        "            \"category2\": \"Usb / Cable\",\n" +
                        "            \"itemType\": \"PHYSICAL\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"enabledInstallments\": [\n" +
                        "        1,\n" +
                        "        2,\n" +
                        "        3,\n" +
                        "        6,\n" +
                        "        9\n" +
                        "    ],\n" +
                        "    \"callbackUrl\": \"https://www.merchant.com/callback\",\n" +
                        "    \"currency\": \"TRY\",\n" +
                        "    \"paidPrice\": \"1.2\"\n" +
                        "}";

                IyzicoService.sendPaymentRequest(jsonBody);

                clearRegisterData();
                String summary = packageName + ";Online Abonelik;" + packagePriceSummary.getText().toString() + ";" + uniqueID;
                // Ödeme ekranına yönlendirmeden önce taksit seçeneği uyarısını göster
                Intent finalIntent = new Intent(Register3.this, Register4.class);
                finalIntent.putExtra("summaryData", summary);
                startActivity(finalIntent);
                finish();
            } else {
                Sneaker.with(Register3.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_term_confirm))
                        .sneakError();
            }
        });

        monthlyButton.setOnClickListener(v -> {
            buttonStatusSwitch(monthlyButton, true);
            buttonStatusSwitch(sixMonthButton, false);
            buttonStatusSwitch(annuallyButton, false);
            packageSetup(1);
            packageName = getString(R.string.plan_1_summary);
        });

        sixMonthButton.setOnClickListener(v -> {
            buttonStatusSwitch(monthlyButton, false);
            buttonStatusSwitch(sixMonthButton, true);
            buttonStatusSwitch(annuallyButton, false);
            packageSetup(2);
            packageName = getString(R.string.plan_2_summary);
        });

        annuallyButton.setOnClickListener(v -> {
            buttonStatusSwitch(monthlyButton, false);
            buttonStatusSwitch(sixMonthButton, false);
            buttonStatusSwitch(annuallyButton, true);
            packageSetup(3);
            packageName = getString(R.string.plan_3_summary);
        });

        editTextExpiryDate.setOnClickListener(v -> {
            hideKeyboard();
            showExpiryDatePicker();
        });

        editTextExpiryDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                hideKeyboard();
                showExpiryDatePicker();
            }
        });

        paymentOptionsButton.setOnClickListener(v -> {
            showPaymentOptions();
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showExpiryDatePicker() {
        ExpiryDatePicker datePickerBottomSheet = new ExpiryDatePicker(this);
        datePickerBottomSheet.show(getSupportFragmentManager(), "datePickerBottomSheet");
    }

    private void showPaymentOptions() {
        PaymentOptions paymentOptionsBottomSheet = new PaymentOptions(option -> selectedPaymentOption = option);
        paymentOptionsBottomSheet.show(getSupportFragmentManager(), "paymentOptionsBottomSheet");
    }

    private boolean areAllFieldsFilled() {
        return !editTextCardNumber.getText().toString().isEmpty()
                && !editTextHolderName.getText().toString().isEmpty()
                && !editTextExpiryDate.getText().toString().isEmpty()
                && !editTextCVV.getText().toString().isEmpty();
    }

    private void summarySection() {
        confirmCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                packageCard.setVisibility(View.GONE);
                packageSummaryCard.setVisibility(View.VISIBLE);
                buttonSummary.setVisibility(View.VISIBLE);
                packagePriceSummary.setVisibility(View.VISIBLE);
                creditCardLayout.setVisibility(View.VISIBLE);
            } else {
                packageCard.setVisibility(View.VISIBLE);
                packageSummaryCard.setVisibility(View.GONE);
                buttonSummary.setVisibility(View.GONE);
                packagePriceSummary.setVisibility(View.GONE);
                creditCardLayout.setVisibility(View.GONE);
            }
        });
    }

    private void buttonStatusSwitch(Button inputButton, boolean buttonStatus) {
        int defaultTextColor = ContextCompat.getColor(this, R.color.darkBaseColor);
        int secondTextColor = ContextCompat.getColor(this, R.color.white);
        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.darkBaseColor));
        ColorStateList colorStateListWhite = ColorStateList.valueOf(getResources().getColor(R.color.white));

        Drawable defaultDrawable = ContextCompat.getDrawable(this, R.drawable.button_fs_no);
        Drawable secondDrawable = ContextCompat.getDrawable(this, R.drawable.button_fs_yes);

        if (buttonStatus) {
            inputButton.setTextColor(secondTextColor);
            inputButton.setBackground(secondDrawable);
            inputButton.setCompoundDrawableTintList(colorStateListWhite);
        } else {
            inputButton.setTextColor(defaultTextColor);
            inputButton.setBackground(defaultDrawable);
            inputButton.setCompoundDrawableTintList(colorStateList);
        }
    }

    private void packageSetup(int stateNumber) {
        if (stateNumber == 1) {
            packagePrice.setText(R.string.plan_1_price);
            packagePromotion.setText(R.string.plan_1_gift);
            package_spec_1.setText(getString(R.string.plan_1_spec_1));
            package_spec_2.setText(getString(R.string.plan_1_spec_2));
            package_spec_3.setText(getString(R.string.plan_1_spec_3));

            buttonSummary.setText(R.string.plan_1_summary);
            packagePriceSummary.setText(R.string.plan_1_price);
        } else if (stateNumber == 2) {
            packagePrice.setText(R.string.plan_2_price);
            packagePromotion.setText(R.string.plan_2_gift);
            package_spec_1.setText(getString(R.string.plan_2_spec_1));
            package_spec_2.setText(getString(R.string.plan_2_spec_2));
            package_spec_3.setText(getString(R.string.plan_2_spec_3));

            buttonSummary.setText(R.string.plan_2_summary);
            packagePriceSummary.setText(R.string.plan_2_price);
        } else {
            packagePrice.setText(R.string.plan_3_price);
            packagePromotion.setText(R.string.plan_3_gift);
            package_spec_1.setText(getString(R.string.plan_3_spec_1));
            package_spec_2.setText(getString(R.string.plan_3_spec_2));
            package_spec_3.setText(getString(R.string.plan_3_spec_3));

            buttonSummary.setText(R.string.plan_3_summary);
            packagePriceSummary.setText(R.string.plan_3_price);
        }
    }

    private void saveData() {
        SharedPreferencesManager.writeSharedPref("cardNumber", editTextCardNumber.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("holderName", editTextHolderName.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("expiryDate", editTextExpiryDate.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("cvv", editTextCVV.getText().toString(), this);
        SharedPreferencesManager.writeSharedPref("confirmCheckBox", confirmCheckBox.isChecked(), this);
    }

    private void loadSavedData() {
        String cardNumber = SharedPreferencesManager.getSharedPref("cardNumber", this, "");
        if (!cardNumber.isEmpty()) {
            editTextCardNumber.setText(cardNumber);
        }

        String holderName = SharedPreferencesManager.getSharedPref("holderName", this, "");
        if (!holderName.isEmpty()) {
            editTextHolderName.setText(holderName);
        }

        String expiryDate = SharedPreferencesManager.getSharedPref("expiryDate", this, "");
        if (!expiryDate.isEmpty()) {
            editTextExpiryDate.setText(expiryDate);
        }

        String cvv = SharedPreferencesManager.getSharedPref("cvv", this, "");
        if (!cvv.isEmpty()) {
            editTextCVV.setText(cvv);
        }

        boolean confirmCheckBoxValue = SharedPreferencesManager.getSharedPref("confirmCheckBox", this, false);
        confirmCheckBox.setChecked(confirmCheckBoxValue);
    }

    private void clearRegisterData() {
        SharedPreferencesManager.writeSharedPref("name", "", this);
        SharedPreferencesManager.writeSharedPref("surname", "", this);
        SharedPreferencesManager.writeSharedPref("idNumber", "", this);
        SharedPreferencesManager.writeSharedPref("birthDate", "", this);
        SharedPreferencesManager.writeSharedPref("countryCode", "", this);
        SharedPreferencesManager.writeSharedPref("phoneNumber", "", this);
        SharedPreferencesManager.writeSharedPref("password", "", this);
        SharedPreferencesManager.writeSharedPref("repeatPassword", "", this);
        SharedPreferencesManager.writeSharedPref("termsAccepted", false, this);
        SharedPreferencesManager.writeSharedPref("addressName", "", this);
        SharedPreferencesManager.writeSharedPref("addressName_name", "", this);
        SharedPreferencesManager.writeSharedPref("addressName_surname", "", this);
        SharedPreferencesManager.writeSharedPref("addressName_countryCode", "", this);
        SharedPreferencesManager.writeSharedPref("addressName_phoneNumber", "", this);
        SharedPreferencesManager.writeSharedPref("city", "", this);
        SharedPreferencesManager.writeSharedPref("district", "", this);
        SharedPreferencesManager.writeSharedPref("neighborhood", "", this);
        SharedPreferencesManager.writeSharedPref("addressDetail", "", this);
        SharedPreferencesManager.writeSharedPref("confirmAddress", false, this);
        SharedPreferencesManager.writeSharedPref("cardNumber", "", this);
        SharedPreferencesManager.writeSharedPref("holderName", "", this);
        SharedPreferencesManager.writeSharedPref("expiryDate", "", this);
        SharedPreferencesManager.writeSharedPref("cvv", "", this);
        SharedPreferencesManager.writeSharedPref("confirmCheckBox", false, this);
    }

    @Override
    public void onExpiryDateSelected(int month, int year) {
        String formattedDate = String.format("%02d/%d", month, year);
        editTextExpiryDate.setText(formattedDate);
    }
}