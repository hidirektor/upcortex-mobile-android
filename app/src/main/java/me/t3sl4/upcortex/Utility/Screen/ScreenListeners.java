package me.t3sl4.upcortex.Utility.Screen;

import android.annotation.SuppressLint;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import me.t3sl4.upcortex.R;

public class ScreenListeners {
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
