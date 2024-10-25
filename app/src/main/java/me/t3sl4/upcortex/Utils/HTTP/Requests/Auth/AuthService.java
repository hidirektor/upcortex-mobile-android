package me.t3sl4.upcortex.Utils.HTTP.Requests.Auth;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.t3sl4.upcortex.Utils.HTTP.HttpHelper;
import me.t3sl4.upcortex.Utils.Service.UserDataService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {
    private static final String REGISTER_URL = "/auth/register";
    private static final String LOGIN_URL = "/auth/login";

    public static void register(Context context, String firstName, String lastName, String email, String dateOfBirth, String address, String password, String dialCode, String phone, String identityNumber, Runnable onSuccess, Runnable onFailure) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            jsonObject.put("email", email);
            jsonObject.put("dateOfBirth", dateOfBirth);
            jsonObject.put("address", address);
            jsonObject.put("password", password);
            jsonObject.put("dialCode", dialCode);
            jsonObject.put("phone", phone);
            jsonObject.put("identityNumber", identityNumber);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", REGISTER_URL, null, jsonObject.toString(), null);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("Register", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("Register", "Failure: " + response.errorBody().string());
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
                Log.e("Register", "Error: " + t.getMessage());
            }
        });
    }

    public static void login(Context context, String identityNumber, String password, Runnable onSuccess, Runnable onFailure) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("identityNumber", identityNumber);
            jsonObject.put("password", password);
            jsonObject.put("platform", "mobile");  // Platform her zaman mobile olacak
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", LOGIN_URL, null, jsonObject.toString(), null);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("Login", "Success: " + responseBody);

                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONObject payload = responseJson.getJSONObject("response");
                        JSONObject userInfo = payload.getJSONObject("userInformation");

                        String userID = userInfo.getString("id");
                        String token = payload.getString("token");

                        String identityNumber = userInfo.getString("identityNumber");
                        String userEmail = userInfo.getString("email");
                        String fullName = userInfo.getString("fullName");
                        String userPhone = userInfo.getString("phone");

                        String userState = userInfo.getString("state");

                        UserDataService.setUserID(context, userID);
                        UserDataService.setAccessToken(context, token);

                        UserDataService.setIdentityNumber(context, identityNumber);
                        UserDataService.seteMail(context, userEmail);
                        UserDataService.setNameSurname(context, fullName);
                        UserDataService.setPhoneNumber(context, userPhone);

                        UserDataService.setUserState(context, userState);

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("Login", "Failure: " + response.errorBody().string());

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
                Log.e("Login", "Error: " + t.getMessage());
            }
        });
    }
}