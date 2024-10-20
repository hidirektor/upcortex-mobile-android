package me.t3sl4.upcortex.UI.Screens.FirstSetup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.Utils.BaseUtil;
import me.t3sl4.upcortex.Utils.Screen.ScreenUtil;
import me.t3sl4.upcortex.Utils.Web.WebViewBottomSheetFragment;

public class FirstSetupError extends AppCompatActivity {

    private Button backToMainButton;
    private Button supportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup_error);

        ScreenUtil.hideNavigationBar(this);
        ScreenUtil.hideStatusBar(this);

        initializeComponents();
        buttonClickListener();
    }

    private void initializeComponents() {
        backToMainButton = findViewById(R.id.backToMainButton);
        supportButton = findViewById(R.id.supportButton);
    }

    private void buttonClickListener() {
        backToMainButton.setOnClickListener(v -> {
            Intent firstSetupIntent = new Intent(FirstSetupError.this, FirstSetup.class);
            startActivity(firstSetupIntent);
            finish();
        });

        supportButton.setOnClickListener(v -> {
            WebViewBottomSheetFragment webViewBottomSheet = new WebViewBottomSheetFragment(BaseUtil.SUPPORT_URL);
            webViewBottomSheet.show(getSupportFragmentManager(), "WebViewBottomSheet");
        });

    }
}
