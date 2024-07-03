package me.t3sl4.upcortex.UI.Screens.Auth.Register;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.NavigationBar.NavigationBarUtil;

public class Register3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_3);

        NavigationBarUtil.hideNavigationBar(this);
    }
}
