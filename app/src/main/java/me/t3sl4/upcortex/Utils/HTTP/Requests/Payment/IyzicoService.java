package me.t3sl4.upcortex.Utils.HTTP.Requests.Payment;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.function.Consumer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import me.t3sl4.upcortex.BuildConfig;
import me.t3sl4.upcortex.Utils.BaseUtil;
import me.t3sl4.upcortex.Utils.SharedPreferences.SharedPreferencesManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IyzicoService {
    private static final String API_KEY = BuildConfig.IYZICO_API_KEY;
    private static final String SECRET_KEY = BuildConfig.IYZICO_SECRET_KEY;

    private static final String BASE_URL = "https://api.iyzipay.com";
    private static final String URI_PATH_CF_START = "/payment/iyzipos/checkoutform/initialize/auth/ecom";
    private static final String URI_PATH_CF_CHECK = "/payment/iyzipos/checkoutform/auth/ecom/detail";

    private static String defaultRandomKey = "123456789";
    private static String defaultCallbackURL = "https://dinamikbeyin.com";
    private static String defaultCurrency = "TRY";
    private static String defaultLocale = "tr";
    private static String defaultPaymentGroup = "PRODUCT";
    private static String defaultProductType = "VIRTUAL";
    private static String defaultCountry = "Turkey";

    public static void sendPaymentRequest(Context context, String planID, String planName, String planPrice,
                                          Consumer<JSONObject> onSuccess, Runnable onFailure) throws JSONException {
        OkHttpClient client = new OkHttpClient();

        String userName = SharedPreferencesManager.getSharedPref("name", context, "");
        String userSurname = SharedPreferencesManager.getSharedPref("surname", context, "");
        String userEmail = SharedPreferencesManager.getSharedPref("eMail", context, "");
        String userAddress = SharedPreferencesManager.getSharedPref("neighboorhood", context, "") + " " + SharedPreferencesManager.getSharedPref("addressDetail", context, "") + " " + SharedPreferencesManager.getSharedPref("zipCode", context, "") + " " + SharedPreferencesManager.getSharedPref("district", context, "") + " " + SharedPreferencesManager.getSharedPref("city", context, "");
        String dialCode = "+" + SharedPreferencesManager.getSharedPref("countryCode", context, "");
        String userPhone = dialCode + SharedPreferencesManager.getSharedPref("phoneNumber", context, "");
        String userIdentity = SharedPreferencesManager.getSharedPref("idNumber", context, "");
        String zipCode = SharedPreferencesManager.getSharedPref("zipCode", context, "");
        String city = SharedPreferencesManager.getSharedPref("city", context, "");

        // Create the buyer object
        JSONObject buyer = new JSONObject();
        buyer.put("id", "BY789");
        buyer.put("name", userName);
        buyer.put("surname", userSurname);
        buyer.put("identityNumber", userIdentity);
        buyer.put("email", userEmail);
        buyer.put("gsmNumber", userPhone);
        //buyer.put("registrationDate", "2013-04-21 15:12:09");
        //buyer.put("lastLoginDate", "2015-10-05 12:43:35");
        buyer.put("registrationAddress", userAddress);
        buyer.put("city", city);
        buyer.put("country", defaultCountry);
        buyer.put("zipCode", zipCode);
        buyer.put("ip", BaseUtil.getUserIp(context));

        JSONObject shippingAddress = new JSONObject();
        shippingAddress.put("address", userAddress);
        shippingAddress.put("zipCode", zipCode);
        shippingAddress.put("contactName", userName + " " + userSurname);
        shippingAddress.put("city", city);
        shippingAddress.put("country", defaultCountry);

        // Create the basket items array
        JSONArray basketItems = new JSONArray();
        basketItems.put(new JSONObject()
                .put("id", planID)
                .put("price", planPrice)
                .put("name", planName)
                .put("category1", "Online Programlar")
                //.put("category2", "Accessories")
                .put("itemType", defaultProductType));

        // Create the main request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("locale", defaultLocale);
        //requestBody.put("conversationId", "123456789");
        requestBody.put("price", planPrice);
        //requestBody.put("basketId", "B67832");
        requestBody.put("paymentGroup", defaultPaymentGroup);
        requestBody.put("buyer", buyer);
        requestBody.put("shippingAddress", shippingAddress);
        requestBody.put("billingAddress", shippingAddress);
        requestBody.put("basketItems", basketItems);
        requestBody.put("enabledInstallments", new JSONArray().put(1).put(2).put(3).put(6).put(9));
        requestBody.put("callbackUrl", defaultCallbackURL);
        requestBody.put("currency", defaultCurrency);
        requestBody.put("paidPrice", planPrice);

        String jsonBody = requestBody.toString();
        String xIyziRnd = new Date().getTime() + defaultRandomKey;
        String authorization = generateAuthorization(jsonBody, xIyziRnd, URI_PATH_CF_START);

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + URI_PATH_CF_START)
                .post(body)
                .addHeader("Authorization", authorization)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String status = jsonResponse.optString("status");

                        if ("success".equals(status)) {
                            if (onSuccess != null) {
                                onSuccess.accept(jsonResponse);  // Başarılı olduğunda response döndür
                            }
                        } else {
                            if(onFailure != null) {
                                onFailure.run();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(onFailure != null) {
                            onFailure.run();
                        }
                    }
                } else {
                    Log.d("Iyzico error: ", response.message());
                    if(onFailure != null) {
                        onFailure.run();
                    }
                }
            }
        });
    }

    public static boolean checkUserPayment(String status, String locale, String systemTime, String token, String checkoutFormContent, String tokenExpireTime, String paymentPageUrl, String payWithIyzicoPageUrl, String signature) {
        Log.d("Odeme kontrol", "Kontrol kısmına geçildi.");
        return false;
    }

    private static String generateAuthorization(String jsonBody, String xIyziRnd, String uriPath) {
        try {
            String randomKey = xIyziRnd;

            String payload = randomKey + uriPath + jsonBody;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacData = mac.doFinal(payload.getBytes("UTF-8"));

            String encryptedData = bytesToHex(hmacData);

            String authorizationString = String.format("apiKey:%s&randomKey:%s&signature:%s", API_KEY, randomKey, encryptedData);

            String base64EncodedAuthorization = Base64.getEncoder().encodeToString(authorizationString.getBytes("UTF-8"));

            return "IYZWSv2 " + base64EncodedAuthorization;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}