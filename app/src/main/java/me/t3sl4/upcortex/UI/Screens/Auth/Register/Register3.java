package me.t3sl4.upcortex.UI.Screens.Auth.Register;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;

import java.util.UUID;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.DatePicker.ExpiryDatePicker;
import me.t3sl4.upcortex.UI.Components.PaymentOptions.PaymentOptions;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Utils.HTTP.Requests.Payment.IyzicoService;
import me.t3sl4.upcortex.Utils.Screen.ScreenUtil;
import me.t3sl4.upcortex.Utils.SharedPreferences.SharedPreferencesManager;
import me.t3sl4.upcortex.Utils.Web.PaymentWebViewBottomSheetFragment;

public class Register3 extends AppCompatActivity implements ExpiryDatePicker.ExpiryDateSelectedListener  {

    private Button firstPlanButton;
    private Button secondPlanButton;
    private Button thirthPlanButton;

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

    private String packageID = "3";
    private String packageName = "Sınava Hazırlık";
    private String packagePriceText = "7800";
    private String selectedPaymentOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_3);

        ScreenUtil.hideNavigationBar(this);

        initializeComponents();
        loadSavedData(); // Kaydedilen verileri yükle
        buttonClickListeners();
        //summarySection();

        uniqueID = UUID.randomUUID().toString();
    }

    private void initializeComponents() {
        firstPlanButton = findViewById(R.id.buttonFirstPlan);
        secondPlanButton = findViewById(R.id.buttonSecondPlan);
        thirthPlanButton = findViewById(R.id.buttonThirthPlan);

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
            if (confirmCheckBox.isChecked()) {
                saveData(); // Verileri kaydet

                try {
                    IyzicoService.sendPaymentRequest(Register3.this, packageID, packageName, packagePriceText,
                            jsonResponse -> {
                                Log.d("Başarılı Ödeme Formu", jsonResponse.toString());

                                String status = jsonResponse.optString("status");
                                String locale = jsonResponse.optString("locale");
                                String systemTime = jsonResponse.optString("systemTime");
                                String token = jsonResponse.optString("token");
                                String checkoutFormContent = jsonResponse.optString("checkoutFormContent");
                                String tokenExpireTime = jsonResponse.optString("tokenExpireTime");
                                String paymentPageUrl = jsonResponse.optString("paymentPageUrl");
                                String payWithIyzicoPageUrl = jsonResponse.optString("payWithIyzicoPageUrl");
                                String signature = jsonResponse.optString("signature");

                                PaymentWebViewBottomSheetFragment webViewFragment = new PaymentWebViewBottomSheetFragment(Register3.this, Register3.this, paymentPageUrl,
                                        status, locale, systemTime, token, checkoutFormContent, tokenExpireTime, paymentPageUrl, payWithIyzicoPageUrl, signature, packageName, packagePriceSummary.getText().toString(), uniqueID);
                                webViewFragment.show(getSupportFragmentManager(), webViewFragment.getTag());
                            },
                            () -> {
                                Sneaker.with(Register3.this)
                                        .setTitle(getString(R.string.error_title))
                                        .setMessage(getString(R.string.error_payment_not_complete))
                                        .sneakError();
                            });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Sneaker.with(Register3.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_term_confirm))
                        .sneakError();
            }
        });

        firstPlanButton.setOnClickListener(v -> {
            buttonStatusSwitch(firstPlanButton, true);
            buttonStatusSwitch(secondPlanButton, false);
            buttonStatusSwitch(thirthPlanButton, false);
            packageSetup(1);

            packageID = "1";
            packageName = getString(R.string.plan_1_summary);
            packagePriceText = "15600";
        });

        secondPlanButton.setOnClickListener(v -> {
            buttonStatusSwitch(firstPlanButton, false);
            buttonStatusSwitch(secondPlanButton, true);
            buttonStatusSwitch(thirthPlanButton, false);
            packageSetup(2);

            packageID = "2";
            packageName = getString(R.string.plan_2_summary);
            packagePriceText = "13600";
        });

        thirthPlanButton.setOnClickListener(v -> {
            buttonStatusSwitch(firstPlanButton, false);
            buttonStatusSwitch(secondPlanButton, false);
            buttonStatusSwitch(thirthPlanButton, true);
            packageSetup(3);

            packageID = "3";
            packageName = getString(R.string.plan_3_summary);
            packagePriceText = "7800";
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

    @Override
    public void onExpiryDateSelected(int month, int year) {
        String formattedDate = String.format("%02d/%d", month, year);
        editTextExpiryDate.setText(formattedDate);
    }
}