package me.t3sl4.upcortex.Utils.Web;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.Utils.HTTP.Requests.Payment.IyzicoService;

public class PaymentWebViewBottomSheetFragment extends BottomSheetDialogFragment {

    private WebView webView;
    private ImageView customProgressBar;
    private String url;
    private ImageView closeIcon;

    // Yeni değişkenler
    private String status;
    private String locale;
    private String systemTime;
    private String token;
    private String checkoutFormContent;
    private String tokenExpireTime;
    private String paymentPageUrl;
    private String payWithIyzicoPageUrl;
    private String signature;

    public PaymentWebViewBottomSheetFragment(String url, String status, String locale, String systemTime, String token,
                                             String checkoutFormContent, String tokenExpireTime, String paymentPageUrl,
                                             String payWithIyzicoPageUrl, String signature) {
        this.url = url;
        this.status = status;
        this.locale = locale;
        this.systemTime = systemTime;
        this.token = token;
        this.checkoutFormContent = checkoutFormContent;
        this.tokenExpireTime = tokenExpireTime;
        this.paymentPageUrl = paymentPageUrl;
        this.payWithIyzicoPageUrl = payWithIyzicoPageUrl;
        this.signature = signature;
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
                Log.d("WebView", "Page finished loading: " + url);
                if(url.equals("https://dinamikbeyin.com/")) {
                    IyzicoService.checkUserPayment(status, locale, systemTime, token, checkoutFormContent, tokenExpireTime, paymentPageUrl, payWithIyzicoPageUrl, signature);
                    dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d("WebView", "Loading URL: " + url);

                if (url.equals("https://dinamikbeyin.com")) {
                    IyzicoService.checkUserPayment(status, locale, systemTime, token, checkoutFormContent, tokenExpireTime, paymentPageUrl, payWithIyzicoPageUrl, signature);
                    dismiss();
                    return true;
                }
                view.loadUrl(url);
                return false;
            }

            // For older API levels
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("WebView", "Loading URL: " + url);

                if (url.equals("https://dinamikbeyin.com")) {
                    IyzicoService.checkUserPayment(status, locale, systemTime, token, checkoutFormContent, tokenExpireTime, paymentPageUrl, payWithIyzicoPageUrl, signature);
                    dismiss();
                    return true;
                }
                view.loadUrl(url);
                return false;
            }
        });

        closeIcon.setOnClickListener(v -> {
            dismiss();
        });

        webView.loadUrl(url);
        Log.d("WebView", "Initial URL loaded: " + url); // Log the initial URL

        return view;
    }
}