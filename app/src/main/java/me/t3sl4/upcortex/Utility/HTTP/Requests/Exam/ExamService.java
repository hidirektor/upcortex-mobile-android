package me.t3sl4.upcortex.Utility.HTTP.Requests.Exam;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.Model.Exam.ExamResponse;
import me.t3sl4.upcortex.Model.Exam.Question;
import me.t3sl4.upcortex.Model.Exam.QuestionCategory;
import me.t3sl4.upcortex.Model.Exam.QuestionOption;
import me.t3sl4.upcortex.Service.UserDataService;
import me.t3sl4.upcortex.Utility.HTTP.HttpHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamService {
    private static final String EXAM_URL = "/user-exam/get-user-exam";

    public static void getUserExam(Context context, Runnable onSuccess, Runnable onFailure) {
        String token = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequest("GET", EXAM_URL, null, null, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("Exam", "Success: " + responseBody);

                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONObject examData = responseJson.getJSONObject("response");

                        ExamResponse examResponse = new ExamResponse();

                        // Exam nesnesini dolduralım
                        JSONObject examJson = examData.getJSONObject("exam");
                        Exam exam = new Exam();
                        exam.setId(examJson.getString("id"));
                        exam.setName(examJson.getString("name"));
                        exam.setApprovalState(examJson.getBoolean("approvalState"));
                        exam.setScale(examJson.getInt("scale"));
                        exam.setMax(examJson.getInt("max"));
                        exam.setDefault(examJson.getBoolean("isDefault"));
                        exam.setExamState(examJson.getString("examState"));

                        examResponse.setExam(exam);

                        // QuestionsByCategories kısmını dolduralım
                        JSONArray categoriesJsonArray = examData.getJSONArray("questionsByCategories");
                        List<QuestionCategory> questionCategories = new ArrayList<>();

                        for (int i = 0; i < categoriesJsonArray.length(); i++) {
                            JSONObject categoryJson = categoriesJsonArray.getJSONObject(i);
                            QuestionCategory category = new QuestionCategory();
                            category.setId(categoryJson.getString("id"));
                            category.setName(categoryJson.getString("name"));
                            category.setOrder(categoryJson.getInt("order"));

                            // Questions kısmını dolduralım
                            JSONArray questionsJsonArray = categoryJson.getJSONArray("questions");
                            List<Question> questions = new ArrayList<>();
                            for (int j = 0; j < questionsJsonArray.length(); j++) {
                                JSONObject questionJson = questionsJsonArray.getJSONObject(j);
                                Question question = new Question();
                                question.setId(questionJson.getString("id"));
                                question.setPreText(questionJson.getString("preText"));
                                question.setMainText(questionJson.getString("mainText"));
                                question.setSubText(questionJson.optString("subText"));
                                question.setFileName(questionJson.optString("fileName"));
                                question.setPoint(questionJson.getString("point"));
                                question.setParent(questionJson.getBoolean("isParent"));
                                question.setDifficulty(questionJson.getString("difficulty"));
                                question.setType(questionJson.getString("type"));
                                question.setParentId(questionJson.optString("parentId", null));

                                // QuestionOptions kısmını dolduralım
                                JSONArray optionsJsonArray = questionJson.getJSONArray("questionOptions");
                                List<QuestionOption> questionOptions = new ArrayList<>();
                                for (int k = 0; k < optionsJsonArray.length(); k++) {
                                    JSONObject optionJson = optionsJsonArray.getJSONObject(k);
                                    QuestionOption option = new QuestionOption();
                                    option.setId(optionJson.getString("id"));
                                    option.setText(optionJson.getString("text"));
                                    option.setCorrect(optionJson.getBoolean("isCorrect"));
                                    option.setType(optionJson.getString("type"));
                                    option.setFileName(optionJson.optString("fileName"));
                                    option.setOrder(optionJson.getInt("order"));

                                    questionOptions.add(option);
                                }
                                question.setQuestionOptions(questionOptions);

                                questions.add(question);
                            }
                            category.setQuestions(questions);
                            questionCategories.add(category);
                        }

                        examResponse.setQuestionsByCategories(questionCategories);

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