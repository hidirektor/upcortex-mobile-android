package me.t3sl4.upcortex.Utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.yariksoffice.lingver.Lingver;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.Service.UserDataService;

public class Utils {
    public static String SUPPORT_URL = "https://github.com/hidirektor";

    public static void setSystemLanguage(Context context) {
        String userLanguage = UserDataService.getSelectedLanguage(context);
        String nextLang;

        if (userLanguage.equals("true")) {
            nextLang = "tr";
        } else if(userLanguage.equals("false")) {
            nextLang = "en";
        } else {
            nextLang = "tr";
        }

        updateLocale(context, nextLang);
    }

    public static void changeSystemLanguage(Context context) {
        String userLanguage = UserDataService.getSelectedLanguage(context);
        String nextLang;

        if (userLanguage.equals("true")) {
            nextLang = "en";
            UserDataService.setSelectedLanguage(context, "false");
        } else {
            nextLang = "tr";
            UserDataService.setSelectedLanguage(context, "true");
        }

        //UserService.updatePreferences(context, UserDataService.getUserID(context), UserDataService.getSelectedLanguage(context), UserDataService.getSelectedNightMode(context));

        updateLocale(context, nextLang);
    }

    public static void updateLocale(Context context, String nextLang) {
        Lingver.getInstance().setLocale(context, nextLang);
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void passwordListener(TextInputEditText password) {
        password.setOnTouchListener(new View.OnTouchListener() {
            boolean isPasswordVisible = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                        if (isPasswordVisible) {
                            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ikon_login_pass, 0, R.drawable.ikon_show_pass, 0);
                        } else {
                            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ikon_login_pass, 0, R.drawable.ikon_hide_pass, 0);
                        }
                        isPasswordVisible = !isPasswordVisible;
                        password.setSelection(password.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
