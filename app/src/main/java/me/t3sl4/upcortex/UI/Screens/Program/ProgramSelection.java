package me.t3sl4.upcortex.UI.Screens.Program;

import static me.t3sl4.upcortex.Utils.BaseUtil.showPermissionPopup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import me.t3sl4.upcortex.Model.Package.Adapter.PackagePagerAdapter;
import me.t3sl4.upcortex.Model.Package.PackageItem;
import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.HorizontalStepper.HorizontalStepper;
import me.t3sl4.upcortex.UI.Screens.General.Dashboard;
import me.t3sl4.upcortex.Utils.Permission.PermissionUtil;
import me.t3sl4.upcortex.Utils.Screen.CustomPageTransformer;
import me.t3sl4.upcortex.Utils.SharedPreferences.SharedPreferencesManager;

public class ProgramSelection extends AppCompatActivity {

    private ImageView notificationButton;

    private ViewPager2 viewPager;
    private TextView bannerText;

    private ViewPager2 productPager;

    FrameLayout darkOverlay;
    ImageView swipeIcon;

    private HorizontalStepper productStepper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_selection);

        initializeComponents();

        swipeAnimation();

        if (PermissionUtil.hasNotificationPermission(this)) {
            if (PermissionUtil.hasBluetoothPermission(this)) {
                continueAppFlow();
            } else {
                showPermissionPopup(3, ProgramSelection.this); // Bluetooth izni
            }
        } else {
            showPermissionPopup(2, ProgramSelection.this);
        }

        initializePackages();
    }

    private void initializeComponents() {
        notificationButton = findViewById(R.id.notificationButton);

        viewPager = findViewById(R.id.viewPager);
        bannerText = findViewById(R.id.imageCounter);

        productPager = findViewById(R.id.productPager);

        darkOverlay = findViewById(R.id.darkOverlay);
        swipeIcon = findViewById(R.id.swipeIcon);

        productStepper = findViewById(R.id.productStepper);
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

    private void swipeAnimation() {
        darkOverlay.setVisibility(View.VISIBLE);

        swipeIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_IN);

        ObjectAnimator rotateLeft = ObjectAnimator.ofFloat(swipeIcon, "rotation", 0f, -5f);
        rotateLeft.setDuration(500);

        ObjectAnimator rotateRight = ObjectAnimator.ofFloat(swipeIcon, "rotation", -5f, 5f);
        rotateRight.setDuration(500);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(rotateLeft, rotateRight);
        animatorSet.setInterpolator(new LinearInterpolator());

        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                swipeIcon.setRotation(0f);
                darkOverlay.setVisibility(View.GONE);
            }
        });
    }

    private void initializePackages() {
        List<PackageItem> packages = new ArrayList<>();
        packages.add(new PackageItem("Unutkanlık ve Bellek (Alzheimer)", "Description for Package 1", 1, R.drawable.ikon_alsheimer));
        packages.add(new PackageItem("Kilo Verme", "Description for Package 2", 2, R.drawable.ikon_diet));
        packages.add(new PackageItem("Sınava Hazırlık", "Description for Package 3", 3, R.drawable.ikon_exam_program));

        PackagePagerAdapter adapter = new PackagePagerAdapter(packages);
        productPager.setAdapter(adapter);

        productPager.setPageTransformer(new CustomPageTransformer());

        productStepper.setStep(productPager.getCurrentItem());

        productPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                productStepper.setStep(position);
            }
        });
    }
}
