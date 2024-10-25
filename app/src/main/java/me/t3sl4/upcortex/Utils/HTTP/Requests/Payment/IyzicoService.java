package me.t3sl4.upcortex.Utils.HTTP.Requests.Payment;

import android.util.Log;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import me.t3sl4.upcortex.BuildConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IyzicoService {

    private static final String URL = "https://api.iyzipay.com/payment/iyzipos/checkoutform/initialize/auth/ecom";
    private static final String API_KEY = BuildConfig.IYZICO_API_KEY;
    private static final String SECRET_KEY = BuildConfig.IYZICO_SECRET_KEY;

    public static void sendPaymentRequest(String jsonBody) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json; charset=utf-8")
        );

        String xIyziRnd = String.valueOf(new Date().getTime()) + "123456789";
        String authorization = generateAuthorization(jsonBody, xIyziRnd);

        Request request = new Request.Builder()
                .url(URL)
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
                    Log.d("Iyzico", responseData);
                } else {
                    Log.d("Iyzico", response.message());
                }
            }
        });
    }

    private static String generateAuthorization(String jsonBody, String xIyziRnd) {
        try {
            String randomKey = xIyziRnd;

            String uriPath = "/payment/iyzipos/checkoutform/initialize/auth/ecom";

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