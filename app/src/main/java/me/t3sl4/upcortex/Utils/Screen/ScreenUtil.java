package me.t3sl4.upcortex.Utils.Screen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ScreenUtil {

    public static void setSwipeListener(Activity currentActivity, Class<?> leftTargetActivity, Class<?> rightTargetActivity) {
        GestureDetector gestureDetector = new GestureDetector(currentActivity, new SwipeGestureDetector(currentActivity, leftTargetActivity, rightTargetActivity));
        currentActivity.findViewById(android.R.id.content).setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private static class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        private Activity currentActivity;
        private Class<?> leftTargetActivity;
        private Class<?> rightTargetActivity;

        SwipeGestureDetector(Activity currentActivity, Class<?> leftTargetActivity, Class<?> rightTargetActivity) {
            this.currentActivity = currentActivity;
            this.leftTargetActivity = leftTargetActivity;
            this.rightTargetActivity = rightTargetActivity;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX < 0) {
                        onSwipeLeft();
                    } else {
                        onSwipeRight();
                    }
                    return true;
                }
            }
            return false;
        }

        private void onSwipeLeft() {
            if(leftTargetActivity != null) {
                Intent intent = new Intent(currentActivity, leftTargetActivity);
                currentActivity.startActivity(intent);
                currentActivity.finish();
            }
        }

        private void onSwipeRight() {
            if(rightTargetActivity != null) {
                Intent intent = new Intent(currentActivity, rightTargetActivity);
                currentActivity.startActivity(intent);
                currentActivity.finish();
            }
        }
    }

    public static void hideStatusBar(Activity mainActivity) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(mainActivity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            mainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(mainActivity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            mainActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        mainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public static void hideNavigationBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static void showNavigationBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    public static void hideNavAndStatus(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void fullScreenMode(Activity activity) {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}