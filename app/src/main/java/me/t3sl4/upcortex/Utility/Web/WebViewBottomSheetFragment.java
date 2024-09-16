package me.t3sl4.upcortex.Utility.Web;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import me.t3sl4.upcortex.R;

public class WebViewBottomSheetFragment extends BottomSheetDialogFragment {

    private WebView webView;
    private ImageView customProgressBar;
    private String url;

    private ImageView closeIcon;

    public WebViewBottomSheetFragment(String url) {
        this.url = url;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview_bottom_sheet, container, false);

        webView = view.findViewById(R.id.webview);
        customProgressBar = view.findViewById(R.id.customProgressBar);
        closeIcon = view.findViewById(R.id.closeIcon);

        Animation loadingAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.macos_loading_animation);
        customProgressBar.startAnimation(loadingAnimation);

        String DESKTOP_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString(DESKTOP_USER_AGENT);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                loadingAnimation.cancel();
                customProgressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });

        closeIcon.setOnClickListener(v -> {
            dismiss();
        });

        webView.loadUrl(url);

        return view;
    }
}