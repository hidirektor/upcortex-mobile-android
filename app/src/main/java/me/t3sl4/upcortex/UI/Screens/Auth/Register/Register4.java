package me.t3sl4.upcortex.UI.Screens.Auth.Register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Screens.Auth.Login;
import me.t3sl4.upcortex.UI.Screens.General.Dashboard;
import me.t3sl4.upcortex.Utils.Screen.ScreenUtil;
import me.t3sl4.upcortex.Utils.Service.UserDataService;

public class Register4 extends AppCompatActivity {

    private TextView planName;
    private TextView orderID;
    private TextView orderType;
    private TextView planPrice;

    private TextView orderAddress;

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_4);

        ScreenUtil.hideNavigationBar(this);

        initializeComponents();
        buttonClickListeners();
        loadReceivedData();
    }

    private void initializeComponents() {
        planName = findViewById(R.id.planNameTextView);
        orderID = findViewById(R.id.orderIDTextView);
        orderType = findViewById(R.id.orderTypeTextView);
        planPrice = findViewById(R.id.planPriceTextView);

        orderAddress = findViewById(R.id.orderAddressTextView);

        loginButton = findViewById(R.id.loginButton);
    }

    private void buttonClickListeners() {
        loginButton.setOnClickListener(v -> {
            if(UserDataService.getAccessToken(Register4.this) != null) {
                Intent intent = new Intent(Register4.this, Dashboard.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(Register4.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadReceivedData() {
        String summaryData = getIntent().getStringExtra("summaryData");
        if (summaryData != null && !summaryData.isEmpty()) {
            String[] dataParts = summaryData.split(";");
            if (dataParts.length == 4) {
                planName.setText(dataParts[0]);
                orderID.setText(dataParts[3]);
                orderType.setText(dataParts[1]);
                planPrice.setText(dataParts[2]);
                orderAddress.setText(UserDataService.getUserAddress(Register4.this));
            }
        }
    }
}
