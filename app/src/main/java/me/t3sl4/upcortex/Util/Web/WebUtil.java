package me.t3sl4.upcortex.Util.Web;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import me.t3sl4.upcortex.UI.Screens.Dialog.WebViewDialogFragment;

public class WebUtil {

    public static void openURL(AppCompatActivity activity, String url) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        WebViewDialogFragment webViewDialogFragment = WebViewDialogFragment.newInstance(url);
        webViewDialogFragment.show(fragmentManager, "web_view_dialog");
    }
}
