package me.t3sl4.upcortex.Utils.HTTP.Requests.Payment;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.t3sl4.upcortex.Model.Subscription.Subscription;
import me.t3sl4.upcortex.Utils.HTTP.HttpHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionService {
    private static final String SUBSCRIPTIONS_URL = "/subscription/list";

    public static void getSubscriptions(Context context, ArrayList<Subscription> subscriptionList, Runnable onSuccess, Runnable onFailure) {
        Call<ResponseBody> call = HttpHelper.makeRequest("GET", SUBSCRIPTIONS_URL, null, null, null);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray payloadArray = jsonResponse.getJSONArray("response");

                        ArrayList<Subscription> subscriptions = new ArrayList<>();
                        for (int i = 0; i < payloadArray.length(); i++) {
                            JSONObject subscriptionJson = payloadArray.getJSONObject(i);

                            Subscription subscription = new Subscription();
                            subscription.setId(subscriptionJson.getString("id"));
                            subscription.setName(subscriptionJson.getString("name"));
                            subscription.setPrice(subscriptionJson.getString("price"));
                            subscription.setDuration(subscriptionJson.getInt("duration"));
                            subscription.setDelayDuration(subscriptionJson.getInt("delayDuration"));
                            subscription.setDurationType(subscriptionJson.getString("durationType"));
                            subscription.setContent(subscriptionJson.getString("content"));
                            subscription.setScope(subscriptionJson.getString("scope"));
                            subscription.setIsActive(subscriptionJson.getString("isActive"));

                            subscriptions.add(subscription);
                        }

                        Log.d("Subscriptions", "Success: " + subscriptions.toString());
                        subscriptionList.addAll(subscriptions);

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                } else {
                    try {
                        Log.e("Subscriptions", "Failure: " + response.errorBody().string());
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Subscriptions", "Error: " + t.getMessage());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }
}
