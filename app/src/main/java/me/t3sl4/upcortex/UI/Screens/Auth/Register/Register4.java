package me.t3sl4.upcortex.UI.Screens.Auth.Register;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;

public class Register4 extends AppCompatActivity {

    private TextView planName;
    private TextView planPrice;
    private TextView orderID;

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_4);

        NavigationBarUtil.hideNavigationBar(this);

        initializeComponents();
    }

    private void initializeComponents() {
        planName = findViewById(R.id.planNameTextView);
        planPrice = findViewById(R.id.planPriceTextView);
        orderID = findViewById(R.id.orderIDTextView);

        loginButton = findViewById(R.id.loginButton);
    }
}
