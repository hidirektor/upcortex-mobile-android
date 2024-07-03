package me.t3sl4.upcortex.UI.Screens.Auth.Register;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.zpj.widget.checkbox.ZCheckBox;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Util.SharedPreferences.SPUtil;

public class Register3 extends AppCompatActivity {

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

    private SPUtil sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_3);

        NavigationBarUtil.hideNavigationBar(this);

        sharedPrefManager = new SPUtil(this);

        initializeComponents();
        loadSavedData(); // Kaydedilen verileri yükle
        buttonClickListeners();
        summarySection();
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
            if (confirmCheckBox.isChecked()) {
                saveData(); // Verileri kaydet
                clearRegisterData();
                // Ödeme ekranına yönlendirmeden önce taksit seçeneği uyarısını göster
                Intent finalIntent = new Intent(Register3.this, Register4.class);
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
        });

        sixMonthButton.setOnClickListener(v -> {
            buttonStatusSwitch(monthlyButton, false);
            buttonStatusSwitch(sixMonthButton, true);
            buttonStatusSwitch(annuallyButton, false);
            packageSetup(2);
        });

        annuallyButton.setOnClickListener(v -> {
            buttonStatusSwitch(monthlyButton, false);
            buttonStatusSwitch(sixMonthButton, false);
            buttonStatusSwitch(annuallyButton, true);
            packageSetup(3);
        });
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
        sharedPrefManager.saveString("cardNumber", editTextCardNumber.getText().toString());
        sharedPrefManager.saveString("holderName", editTextHolderName.getText().toString());
        sharedPrefManager.saveString("expiryDate", editTextExpiryDate.getText().toString());
        sharedPrefManager.saveString("cvv", editTextCVV.getText().toString());
        sharedPrefManager.saveBoolean("confirmCheckBox", confirmCheckBox.isChecked());
    }

    private void loadSavedData() {
        String cardNumber = sharedPrefManager.getString("cardNumber");
        if (cardNumber != null && !cardNumber.isEmpty()) {
            editTextCardNumber.setText(cardNumber);
        }

        String holderName = sharedPrefManager.getString("holderName");
        if (holderName != null && !holderName.isEmpty()) {
            editTextHolderName.setText(holderName);
        }

        String expiryDate = sharedPrefManager.getString("expiryDate");
        if (expiryDate != null && !expiryDate.isEmpty()) {
            editTextExpiryDate.setText(expiryDate);
        }

        String cvv = sharedPrefManager.getString("cvv");
        if (cvv != null && !cvv.isEmpty()) {
            editTextCVV.setText(cvv);
        }

        boolean confirmCheckBoxValue = sharedPrefManager.getBoolean("confirmCheckBox");
        confirmCheckBox.setChecked(confirmCheckBoxValue);
    }

    private void clearRegisterData() {
        sharedPrefManager.remove("name");
        sharedPrefManager.remove("surname");
        sharedPrefManager.remove("idNumber");
        sharedPrefManager.remove("birthDate");
        sharedPrefManager.remove("countryCode");
        sharedPrefManager.remove("phoneNumber");
        sharedPrefManager.remove("password");
        sharedPrefManager.remove("repeatPassword");
        sharedPrefManager.remove("termsAccepted");
        sharedPrefManager.remove("addressName");
        sharedPrefManager.remove("addressName_name");
        sharedPrefManager.remove("addressName_surname");
        sharedPrefManager.remove("addressName_countryCode");
        sharedPrefManager.remove("addressName_phoneNumber");
        sharedPrefManager.remove("city");
        sharedPrefManager.remove("district");
        sharedPrefManager.remove("neighborhood");
        sharedPrefManager.remove("addressDetail");
        sharedPrefManager.remove("confirmAddress");
        sharedPrefManager.remove("cardNumber");
        sharedPrefManager.remove("holderName");
        sharedPrefManager.remove("expiryDate");
        sharedPrefManager.remove("cvv");
        sharedPrefManager.remove("confirmCheckBox");
    }
}