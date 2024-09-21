package me.t3sl4.upcortex.Utility.HTTP.Requests.Exam;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import me.t3sl4.upcortex.Model.Exam.CategoryClassification;
import me.t3sl4.upcortex.Model.Exam.Difficulty;
import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.Model.Exam.ExamState;
import me.t3sl4.upcortex.Model.Exam.GeneralClassification;
import me.t3sl4.upcortex.Model.Exam.Question;
import me.t3sl4.upcortex.Model.Exam.QuestionCategory;
import me.t3sl4.upcortex.Model.Exam.QuestionOption;
import me.t3sl4.upcortex.Model.Exam.QuestionType;
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
                                    beforeText,
                                    0
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

    public static void getExamDetail(Context context, Runnable onSuccess, Runnable onFailure, String examID, Exam currentExam) {
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

                        Gson gson = new GsonBuilder().create();
                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONObject examData = responseJson.getJSONObject("response");
                        JSONArray questionsByCategories = examData.getJSONArray("questionsByCategories");
                        JSONArray questionCategoriesClassification = examData.getJSONArray("questionCategoriesClassification");
                        JSONArray generalClassification = examData.getJSONArray("generalClassification");

                        LinkedList<QuestionCategory> questionCategoryList = new LinkedList<>();
                        try {
                            for (int i = 0; i < questionCategoriesClassification.length(); i++) {
                                JSONObject categoryObject = questionCategoriesClassification.getJSONObject(i);

                                String categoryId = categoryObject.getString("id");
                                String categoryName = categoryObject.getString("name");
                                int categoryOrder = categoryObject.getInt("order");

                                JSONArray subCategoriesArray = categoryObject.getJSONArray("subCategories");
                                LinkedList<CategoryClassification> categoryClassifications = new LinkedList<>();

                                for (int j = 0; j < subCategoriesArray.length(); j++) {
                                    JSONObject subCategoryObject = subCategoriesArray.getJSONObject(j);

                                    String subCategoryId = subCategoryObject.getString("id");
                                    String subCategoryName = subCategoryObject.getString("name");
                                    int minVal = subCategoryObject.getInt("min");
                                    int maxVal = subCategoryObject.getInt("max");

                                    CategoryClassification categoryClassification = new CategoryClassification(
                                            subCategoryId,
                                            subCategoryName,
                                            minVal,
                                            maxVal
                                    );

                                    categoryClassifications.add(categoryClassification);
                                }

                                QuestionCategory questionCategory = new QuestionCategory(
                                        categoryId,
                                        categoryName,
                                        categoryOrder,
                                        categoryClassifications
                                );

                                questionCategoryList.add(questionCategory);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        LinkedList<GeneralClassification> examClassifications = new LinkedList<>();
                        try {
                            for (int i = 0; i < generalClassification.length(); i++) {
                                JSONObject classificationObject = generalClassification.getJSONObject(i);

                                String classificationId = classificationObject.getString("id");
                                String classificationName = classificationObject.getString("name");
                                int minVal = classificationObject.getInt("min");
                                int maxVal = classificationObject.getInt("max");

                                GeneralClassification classification = new GeneralClassification(
                                        classificationId,
                                        classificationName,
                                        minVal,
                                        maxVal
                                );

                                examClassifications.add(classification);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        LinkedList<Question> questions = new LinkedList<>();
                        try {
                            for (int i = 0; i < questionsByCategories.length(); i++) {
                                JSONObject category = questionsByCategories.getJSONObject(i);
                                String categoryName = category.getString("name");
                                int categoryOrder = category.getInt("order");

                                JSONArray questionsArray = category.getJSONArray("questions");

                                for (int j = 0; j < questionsArray.length(); j++) {
                                    JSONObject questionJson = questionsArray.getJSONObject(j);

                                    String id = questionJson.getString("id");
                                    String preText = questionJson.optString("preText", "");
                                    String mainText = questionJson.optString("mainText", "");
                                    String subText = questionJson.optString("subText", "");
                                    String fileName = questionJson.isNull("fileName") ? null : questionJson.getString("fileName");
                                    float point = Float.parseFloat(questionJson.getString("point"));
                                    boolean isParent = questionJson.getBoolean("isParent");

                                    Difficulty difficulty = Difficulty.valueOf(questionJson.getString("difficulty").toUpperCase(Locale.ENGLISH));
                                    QuestionType type = QuestionType.valueOf(questionJson.getString("type").toUpperCase(Locale.ENGLISH));

                                    String parentId = questionJson.optString("parentId", null);
                                    String format = questionJson.getString("format");
                                    int correctOptionsCount = questionJson.getInt("correctOptionsCount");
                                    int totalOptionsCount = questionJson.getInt("totalOptionsCount");

                                    LinkedList<QuestionOption> questionOptions = new LinkedList<>();

                                    JSONArray optionsArray = questionJson.getJSONArray("questionOptions");

                                    for (int k = 0; k < optionsArray.length(); k++) {
                                        JSONObject optionJson = optionsArray.getJSONObject(k);

                                        String optionId = optionJson.getString("id");
                                        String text = optionJson.getString("text");
                                        boolean isCorrect = optionJson.getBoolean("isCorrect");
                                        String optionType = optionJson.getString("type");
                                        String optionFileName = optionJson.isNull("fileName") ? null : optionJson.getString("fileName");
                                        int order = optionJson.getInt("order");

                                        QuestionOption questionOption = new QuestionOption(
                                                optionId,
                                                text,
                                                isCorrect,
                                                optionType,
                                                optionFileName,
                                                order
                                        );
                                        questionOptions.add(questionOption);
                                    }

                                    Question question = new Question(
                                            categoryName,
                                            categoryOrder,
                                            id,
                                            preText,
                                            mainText,
                                            subText,
                                            fileName,
                                            point,
                                            isParent,
                                            difficulty,
                                            type,
                                            parentId,
                                            format,
                                            correctOptionsCount,
                                            totalOptionsCount,
                                            questionOptions
                                    );
                                    questions.add(question);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        currentExam.setQuestionCategories(questionCategoryList);
                        currentExam.setExamClassifications(examClassifications);
                        currentExam.setQuestions(questions);

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        if(onFailure != null) {
                            onFailure.run();
                        }
                    }
                } else {
                    if(onFailure != null) {
                        onFailure.run();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Exam", "Error: " + t.getMessage());
                if(onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }
}