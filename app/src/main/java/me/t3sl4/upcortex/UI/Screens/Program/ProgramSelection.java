package me.t3sl4.upcortex.UI.Screens.Program;

import static me.t3sl4.upcortex.Utils.BaseUtil.showPermissionPopup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Screens.General.Dashboard;
import me.t3sl4.upcortex.Utils.Permission.PermissionUtil;
import me.t3sl4.upcortex.Utils.SharedPreferences.SharedPreferencesManager;

public class ProgramSelection extends AppCompatActivity {

    private ImageView notificationButton;

    private ViewPager2 viewPager;
    private TextView bannerText;

    private LinearLayout alzheimerButton;
    private LinearLayout diyetButton;
    private LinearLayout sinavaHazirlikButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_selection);

        initializeComponents();

        if (PermissionUtil.hasNotificationPermission(this)) {
            if (PermissionUtil.hasBluetoothPermission(this)) {
                continueAppFlow();
            } else {
                showPermissionPopup(3, ProgramSelection.this); // Bluetooth izni
            }
        } else {
            showPermissionPopup(2, ProgramSelection.this);
        }
    }

    private void initializeComponents() {
        notificationButton = findViewById(R.id.notificationButton);

        viewPager = findViewById(R.id.viewPager);
        bannerText = findViewById(R.id.imageCounter);

        alzheimerButton = findViewById(R.id.alzheimerButton);
        diyetButton = findViewById(R.id.diyetButton);
        sinavaHazirlikButton = findViewById(R.id.sinavaHazirlikButton);
    }

    private void redirectDashboard() {
        String lastScreen = SharedPreferencesManager.getSharedPref("lastScreen", this, "");

        if(lastScreen != null || !lastScreen.isEmpty()) {
            if(lastScreen.equals("sinavaHazirlik")) {
                Intent sinavIntent = new Intent(ProgramSelection.this, Dashboard.class);
                startActivity(sinavIntent);
                finish();
            } else if(lastScreen.equals("alzheimer")) {
                Intent sinavIntent = new Intent(ProgramSelection.this, Dashboard.class);
                startActivity(sinavIntent);
                finish();
            } else if(lastScreen.equals("diyet")) {
                Intent sinavIntent = new Intent(ProgramSelection.this, Dashboard.class);
                startActivity(sinavIntent);
                finish();
            }
        }
    }

    private void buttonClickListeners() {
        alzheimerButton.setOnClickListener(v -> {
            Intent sinavIntent = new Intent(ProgramSelection.this, Dashboard.class);
            SharedPreferencesManager.writeSharedPref("lastScreen", "alzheimer", this);
            startActivity(sinavIntent);
            finish();
        });

        diyetButton.setOnClickListener(v -> {
            Intent sinavIntent = new Intent(ProgramSelection.this, Dashboard.class);
            SharedPreferencesManager.writeSharedPref("lastScreen", "diyet", this);
            startActivity(sinavIntent);
            finish();
        });

        sinavaHazirlikButton.setOnClickListener(v -> {
            Intent sinavIntent = new Intent(ProgramSelection.this, Dashboard.class);
            SharedPreferencesManager.writeSharedPref("lastScreen", "sinavaHazirlik", this);
            startActivity(sinavIntent);
            finish();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!PermissionUtil.hasNotificationPermission(this)) {
                    showPermissionPopup(2, ProgramSelection.this);
                } else if (!PermissionUtil.hasBluetoothPermission(this)) {
                    showPermissionPopup(3, ProgramSelection.this);
                } else {
                    continueAppFlow();
                }
            } else {
                showPermissionPopup(1, ProgramSelection.this);
            }
        } else if (requestCode == PermissionUtil.NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!PermissionUtil.hasBluetoothPermission(this)) {
                    showPermissionPopup(3, ProgramSelection.this);
                } else {
                    continueAppFlow();
                }
            } else {
                showPermissionPopup(2, ProgramSelection.this);
            }
        } else if (requestCode == PermissionUtil.BLUETOOTH_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                continueAppFlow();
            } else {
                showPermissionPopup(3, ProgramSelection.this); // Bluetooth izni
            }
        }
    }

    private void continueAppFlow() {
        redirectDashboard();

        buttonClickListeners();
    }
}
