package me.t3sl4.upcortex.Util.Screen;

import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

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
}