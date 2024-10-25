package me.t3sl4.upcortex.Utils.Screen;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class CustomPageTransformer implements ViewPager2.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) {
            page.setAlpha(0);
        } else if (position <= 0) {
            page.setAlpha(1 + position);
            page.setTranslationX(0);
        } else if (position <= 1) {
            page.setAlpha(1 - position);
            page.setTranslationX(-position * page.getWidth() / 4);
        } else {
            page.setAlpha(0);
        }
    }
}