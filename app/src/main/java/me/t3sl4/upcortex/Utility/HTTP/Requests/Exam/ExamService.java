package me.t3sl4.upcortex.Utility.HTTP.Requests.Exam;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.Model.Exam.ExamState;
import me.t3sl4.upcortex.Service.UserDataService;
import me.t3sl4.upcortex.Utility.HTTP.HttpHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamService {
    private static final String USER_ALL_EXAMS = "/exam/list";
    private static final String USER_EXAM_URL = "/user-exam/get-user-exam";

    public static void getAllExams(Context context, Runnable onSuccess, Runnable onFailure, LinkedList<Exam> examList) {
        String token = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequest("GET", USER_ALL_EXAMS, null, null, token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("getAllExams", "Success: " + responseBody);

                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONArray examsData = responseJson.getJSONArray("response");

                        for(int i=0; i<examsData.length(); i++) {
                            JSONObject examJson = examsData.getJSONObject(i);

                            String examID = examJson.getString("id");
                            String examName = examJson.getString("name");
                            boolean approvalState = examJson.getBoolean("approvalState");
                            int examScale = examJson.getInt("scale");
                            int examMax = examJson.getInt("max");
                            boolean isDefault = examJson.getBoolean("isDefault");
                            String examCreatedBy = examJson.getString("createdUserId");
                            String examApprovedBy = examJson.getString("approvedUserId");
                            String userExamState = examJson.getString("userExamState");
                            int examTime = examJson.getInt("time");
                            String examDescription = examJson.getString("description");
                            String examInstructions = examJson.getString("instructions");
                            String beforeText = examJson.getString("beforeExamText");

                            Exam exam = new Exam(
                                    examID,
                                    examName,
                                    approvalState,
                                    examScale,
                                    examMax,
                                    isDefault,
                                    examCreatedBy,
                                    examApprovedBy,
                                    ExamState.valueOf(userExamState.toUpperCase(Locale.ENGLISH)),
                                    examTime,
                                    examDescription,
                                    examInstructions,
                                    beforeText
                            );

                            examList.add(exam);

                            if(onSuccess != null) {
                                onSuccess.run();
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("Exam", "Failure: " + response.errorBody().string());

                        if (onFailure != null) {
                            onFailure.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("Exam", "Error: " + throwable.getMessage());
            }
        });
    }

    public static void getExamDetail(Context context, Runnable onSuccess, Runnable onFailure, String examID) {
        String token = UserDataService.getAccessToken(context);
        Map<String, String> params = new HashMap<>();
        params.put("examId", examID);

        Call<ResponseBody> call = HttpHelper.makeRequest("GET", USER_EXAM_URL, params, null, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("Exam", "Success: " + responseBody);

                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONObject examData = responseJson.getJSONObject("response");

                        //Tüm sınavlar için question list çekilecek

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("Exam", "Failure: " + response.errorBody().string());

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
                Log.e("Exam", "Error: " + t.getMessage());
            }
        });
    }
}