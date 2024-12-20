package me.t3sl4.upcortex.Utils.Web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONException;

import java.util.concurrent.atomic.AtomicReference;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.UI.Screens.Auth.Register.Register4;
import me.t3sl4.upcortex.Utils.BaseUtil;
import me.t3sl4.upcortex.Utils.HTTP.Requests.Payment.IyzicoService;
import me.t3sl4.upcortex.Utils.Service.UserDataService;

public class PaymentWebViewBottomSheetFragment extends BottomSheetDialogFragment {

    private WebView webView;
    private ImageView customProgressBar;
    private String url;
    private ImageView closeIcon;

    private Activity activity;
    private Context context;

    // Yeni değişkenler
    private String status;
    private String locale;
    private String systemTime;
    private String token;
    private String paymentPageUrl;
    private String payWithIyzicoPageUrl;
    private String signature;

    public static String packageName, packagePriceSummary, packageId, packageCode;
    AtomicReference<String> uniqueID = new AtomicReference<>();

    public PaymentWebViewBottomSheetFragment(Activity activity, Context context, String url, String status, String locale, String systemTime,
                                             String token, String paymentPageUrl,
                                             String payWithIyzicoPageUrl, String signature, String packageName, String packagePriceSummary, String packageId, String packageCode) {
        this.url = url;
        this.status = status;
        this.locale = locale;
        this.systemTime = systemTime;
        this.token = token;
        this.paymentPageUrl = paymentPageUrl;
        this.payWithIyzicoPageUrl = payWithIyzicoPageUrl;
        this.signature = signature;
        this.activity = activity;
        this.context = context;
        this.packageName = packageName;
        this.packagePriceSummary = packagePriceSummary;
        this.packageId = packageId;
        this.packageCode = packageCode;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view_payment, container, false);

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
                    try {
                        IyzicoService.checkUserPayment(status, locale, systemTime, token, paymentPageUrl, payWithIyzicoPageUrl, signature, () -> {
                            IyzicoService.createSubscription(context,
                                    status, locale, systemTime, token, paymentPageUrl, payWithIyzicoPageUrl, signature, uniqueID, packageId, packageCode, UserDataService.getUserAddressId(context), () -> {
                                        afterPaymentOperations(uniqueID);
                                    }, () -> {
                                        dismiss();
                                        Sneaker.with(activity)
                                                .setTitle(getString(R.string.error_title))
                                                .setMessage(getString(R.string.error_payment_not_complete))
                                                .sneakError();
                                    });
                        }, () -> {
                            dismiss();
                            Sneaker.with(activity)
                                    .setTitle(getString(R.string.error_title))
                                    .setMessage(getString(R.string.error_payment_not_complete))
                                    .sneakError();
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d("WebView", "Loading URL: " + url);

                if (url.equals("https://dinamikbeyin.com")) {
                    try {
                        IyzicoService.checkUserPayment(status, locale, systemTime, token, paymentPageUrl, payWithIyzicoPageUrl, signature, () -> {
                            IyzicoService.createSubscription(context,
                                    status, locale, systemTime, token, paymentPageUrl, payWithIyzicoPageUrl, signature, uniqueID, packageId, packageCode, UserDataService.getUserAddressId(context), () -> {
                                        afterPaymentOperations(uniqueID);
                                    }, () -> {
                                        dismiss();
                                        Sneaker.with(activity)
                                                .setTitle(getString(R.string.error_title))
                                                .setMessage(getString(R.string.error_payment_not_complete))
                                                .sneakError();
                                    });
                        }, () -> {
                            dismiss();
                            Sneaker.with(activity)
                                    .setTitle(getString(R.string.error_title))
                                    .setMessage(getString(R.string.error_payment_not_complete))
                                    .sneakError();
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
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
                    try {
                        IyzicoService.checkUserPayment(status, locale, systemTime, token, paymentPageUrl, payWithIyzicoPageUrl, signature, () -> {
                            IyzicoService.createSubscription(context,
                                    status, locale, systemTime, token, paymentPageUrl, payWithIyzicoPageUrl, signature, uniqueID, packageId, packageCode, UserDataService.getUserAddressId(context), () -> {
                                        afterPaymentOperations(uniqueID);
                            }, () -> {
                                        dismiss();
                                        Sneaker.with(activity)
                                                .setTitle(getString(R.string.error_title))
                                                .setMessage(getString(R.string.error_payment_not_complete))
                                                .sneakError();
                                    });
                        }, () -> {
                            dismiss();
                            Sneaker.with(activity)
                                    .setTitle(getString(R.string.error_title))
                                    .setMessage(getString(R.string.error_payment_not_complete))
                                    .sneakError();
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
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

    private void afterPaymentOperations(AtomicReference<String> uniqueID) {
        BaseUtil.clearRegisterData(context);
        dismiss();
        String summary = packageName + ";Online Abonelik;" + packagePriceSummary + ";" + uniqueID.get();

        Intent finalIntent = new Intent(context, Register4.class);
        finalIntent.putExtra("summaryData", summary);
        startActivity(finalIntent);
        activity.finish();
    }
}