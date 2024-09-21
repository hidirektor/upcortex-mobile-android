package me.t3sl4.upcortex.UI.Screens.Exam;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import me.t3sl4.upcortex.Model.Exam.CategoryInfo;
import me.t3sl4.upcortex.Model.Exam.Difficulty;
import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.Model.Exam.Question;
import me.t3sl4.upcortex.Model.Exam.QuestionCategory;
import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.Utility.Screen.ScreenUtil;

public class ExamProcess extends AppCompatActivity {
    private Exam receivedExam;

    //DifficultyStars
    private ImageView difficultyStarOne;
    private ImageView difficultyStarTwo;
    private ImageView difficultyStarThree;

    private int questionTime;

    private List<CategoryInfo> categoryInfoList = new ArrayList<>();
    private int examPoint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_process);

        String examJson = getIntent().getStringExtra("examJson");
        if (examJson != null) {
            Gson gson = new Gson();
            receivedExam = gson.fromJson(examJson, Exam.class);
            Log.d("ExamProcess", "Exam nesnesi başarıyla alındı: " + receivedExam.getExamName());
        } else {
            Log.e("ExamProcess", "Exam JSON verisi alınamadı!");
        }

        ScreenUtil.hideNavAndStatus(ExamProcess.this);
        ScreenUtil.fullScreenMode(ExamProcess.this);

        initializeComponents();
        processExamData();
    }

    private void initializeComponents() {
        difficultyStarOne = findViewById(R.id.difficultyStarOne);
        difficultyStarTwo = findViewById(R.id.difficultyStarTwo);
        difficultyStarThree = findViewById(R.id.difficultyStarThree);
    }

    private void processExamData() {
        if (receivedExam != null && receivedExam.getQuestions() != null) {
            LinkedList<QuestionCategory> categories = receivedExam.getQuestionCategories();
            if (categories != null) {
                Log.d("ProcessExamData", "Kategori sayısı: " + categories.size());
                for (QuestionCategory category : categories) {
                    Log.d("ProcessExamData", "Kategori: " + category.getName() + ", Sıra: " + category.getOrder());
                    CategoryInfo info = new CategoryInfo(category.getName(), category.getOrder());
                    categoryInfoList.add(info);
                }

                // Kategori sırasına göre sırala
                Collections.sort(categoryInfoList, new Comparator<CategoryInfo>() {
                    @Override
                    public int compare(CategoryInfo o1, CategoryInfo o2) {
                        return Integer.compare(o1.getOrder(), o2.getOrder());
                    }
                });

                // Sıralandıktan sonra logla
                Log.d("ProcessExamData", "Sıralandıktan sonra kategori listesi:");
                for (CategoryInfo info : categoryInfoList) {
                    Log.d("ProcessExamData", "Kategori: " + info.getName() + ", Sıra: " + info.getOrder());
                }
            } else {
                Log.d("ProcessExamData", "Kategoriler null veya boş");
            }

            // Soruları kategorilere göre say
            LinkedList<Question> questions = receivedExam.getQuestions();
            if (questions != null) {
                Log.d("ProcessExamData", "Soru sayısı: " + questions.size());
                for (Question question : questions) {
                    String categoryName = question.getCategoryName();
                    Log.d("ProcessExamData", "Soru kategorisi: " + categoryName);
                    boolean matched = false;
                    for (CategoryInfo info : categoryInfoList) {
                        if (info.getName().trim().equalsIgnoreCase(categoryName.trim())) {
                            info.incrementQuestionCount();
                            matched = true;
                            Log.d("ProcessExamData", "Kategori eşleşti: " + categoryName);
                            break;
                        }
                    }
                    if (!matched) {
                        Log.d("ProcessExamData", "Kategori eşleşmedi: " + categoryName);
                    }
                }
            } else {
                Log.d("ProcessExamData", "Sorular null veya boş");
            }
            displayCategoryInfo();
        } else {
            Log.d("ProcessExamData", "receivedExam veya questions null");
        }
    }

    private void updateUserPoints(String categoryName, int points) {
        for (CategoryInfo info : categoryInfoList) {
            if (info.getName().equals(categoryName)) {
                info.addUserPoint(points);
                break;
            }
        }
    }

    private void updateExamPoint(int points) {
        examPoint += points;
    }

    private void displayCategoryInfo() {
        for (CategoryInfo info : categoryInfoList) {
            Log.d("CategoryInfo", "Kategori: " + info.getName() +
                    ", Sıra: " + info.getOrder() +
                    ", Soru Sayısı: " + info.getQuestionCount() +
                    ", Kullanıcı Puanı: " + info.getUserPoint());
        }

        Log.d("ExamPoint", "Toplam Sınav Puanı: " + examPoint);
    }

    private void difficultyMode(Difficulty difficultyEnum) {
        if(difficultyEnum.name().equals("beginner")) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.darkOnTop);
            setStarColor(difficultyStarThree, R.color.darkOnTop);
        } else if(difficultyEnum.name().equals("medium")) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.warningColor);
            setStarColor(difficultyStarThree, R.color.darkOnTop);
        } else if(difficultyEnum.name().equals("hard")) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.warningColor);
            setStarColor(difficultyStarThree, R.color.warningColor);
        }
    }

    private void setStarColor(ImageView star, int colorResId) {
        int color = ContextCompat.getColor(this, colorResId);

        star.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    }
}
