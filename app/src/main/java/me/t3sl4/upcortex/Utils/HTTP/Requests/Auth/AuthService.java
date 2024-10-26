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
    private static final String PROFILE_URL = "/user/profile";
    private static final String ADDRESS_URL = "/address/create";

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

    public static void createAddress(Context context, String addressName, String firstName, String lastName, String dialCode, String phone, String city, String district, String neighborhood, String description, Runnable onSuccess, Runnable onFailure) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", addressName);
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            jsonObject.put("dialCode", dialCode);
            jsonObject.put("phone", phone);
            jsonObject.put("city", city);
            jsonObject.put("district", district);
            jsonObject.put("neighbourhood", neighborhood);
            jsonObject.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", ADDRESS_URL, null, jsonObject.toString(), UserDataService.getAccessToken(context));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("Create Address", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("Create Address", "Failure: " + response.errorBody().string());
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
                Log.e("Create Address", "Error: " + t.getMessage());
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

    public static void getProfile(Context context, Runnable onSuccess, Runnable onFailure) {
        Call<ResponseBody> call = HttpHelper.makeRequest("GET", PROFILE_URL, null, null, UserDataService.getAccessToken(context));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("Get Profile", "Success: " + responseBody);

                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONObject payload = responseJson.getJSONObject("response");
                        JSONObject addressBlock = payload.getJSONObject("address");

                        String firstName = payload.getString("firstName");
                        String lastName = payload.getString("lastName");
                        String address = payload.getString("address");
                        String dialCode = payload.getString("dialCode");
                        String registrationState = payload.getString("registrationState");

                        String addressId = addressBlock.getString("id");

                        if(address != null && !address.isEmpty()) {
                            String[] addressParts = address.split(" ");
                            String zipCode = addressParts[addressParts.length - 2];
                            String city = addressParts[addressParts.length - 1];

                            UserDataService.setUserZipCode(context, zipCode);
                            UserDataService.setUserCity(context, city);
                        }

                        UserDataService.setUserFirstName(context, firstName);
                        UserDataService.setUserLastName(context, lastName);
                        UserDataService.setUserAddress(context, address);
                        UserDataService.setUserDialCode(context, dialCode);
                        UserDataService.setUserState(context, registrationState);
                        UserDataService.setUserAddressId(context, addressId);

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("Get Profile", "Failure: " + response.errorBody().string());

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