package me.t3sl4.upcortex.UI.Screens.Auth.Register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.zpj.widget.checkbox.ZCheckBox;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;

public class Register2 extends AppCompatActivity {

    private TextInputEditText addressName;
    private TextInputEditText name;
    private TextInputEditText surname;
    private Spinner countryCode;
    private TextInputEditText phoneNumber;
    private TextInputEditText city;
    private TextInputEditText district;
    private TextInputEditText neighborhood;
    private TextInputEditText addressDetail;
    private ZCheckBox confirmAddress;
    private TextView termsViewer;
    private Button backButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);

        NavigationBarUtil.hideNavigationBar(this);

        initializeComponents();
        buttonClickListeners();
    }

    private void initializeComponents() {
        addressName = findViewById(R.id.editTextAddresName);
        name = findViewById(R.id.editTextName);
        surname = findViewById(R.id.editTextSurname);
        countryCode = findViewById(R.id.country_code_spinner);
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
            if(confirmAddress.isChecked()) {
                Intent intent = new Intent(Register2.this, Register3.class);
                startActivity(intent);
                finish();
            } else {
                //Uyarı mesajı vermen gereken yer
            }
        });
    }
}
