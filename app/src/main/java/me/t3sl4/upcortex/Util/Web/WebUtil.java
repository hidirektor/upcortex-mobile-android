package me.t3sl4.upcortex.Util.Web;

import android.app.Activity;
import android.app.FragmentManager;

import me.t3sl4.upcortex.UI.Screens.Dialog.WebViewDialogFragment;


public class WebUtil {

    public static void openURL(Activity activity, String url) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        WebViewDialogFragment webViewDialogFragment = WebViewDialogFragment.newInstance(url);
        webViewDialogFragment.show(fragmentManager, "web_view_dialog");
    }
}
