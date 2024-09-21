package me.t3sl4.upcortex.UI.Screens.Exam;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import me.t3sl4.upcortex.UI.Components.CircularCountdown.CircularCountdownView;
import me.t3sl4.upcortex.Utility.Screen.ScreenUtil;

public class ExamProcess extends AppCompatActivity {
    private Exam receivedExam;

    //DifficultyStars
    private ImageView difficultyStarOne;
    private ImageView difficultyStarTwo;
    private ImageView difficultyStarThree;

    private TextView difficultyText;
    private TextView categoryOrder;
    private TextView categoryName;
    private CircularCountdownView circularCountdownView;

    private Button preTextButton;

    private LinearLayout imageQuestionLayout;

    //Image Question Layout
    private ImageView imageView1_1;
    private ImageView imageView1_2;
    private ImageView imageView1_3;
    private ImageView imageView2_1;
    private ImageView imageView2_2;
    private ImageView imageView2_3;
    private ImageView imageView3_1;
    private ImageView imageView3_2;
    private ImageView imageView3_3;
    private ImageView imageView4_1;
    private ImageView imageView4_2;
    private ImageView imageView4_3;

    private int questionTime;

    private List<CategoryInfo> categoryInfoList = new ArrayList<>();
    private int examPoint = 0;

    private List<ImageView> imageViewList = new ArrayList<>();

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

        difficultyText = findViewById(R.id.difficultyText);
        categoryOrder = findViewById(R.id.categoryOrder);
        categoryName = findViewById(R.id.categoryName);
        circularCountdownView = findViewById(R.id.circularCountdownView);

        preTextButton = findViewById(R.id.preTextButton);

        imageQuestionLayout = findViewById(R.id.imageQuestionLayout);

        //Image Question Layout
        imageView1_1 = findViewById(R.id.imageView1_1);
        imageView1_2 = findViewById(R.id.imageView1_2);
        imageView1_3 = findViewById(R.id.imageView1_3);
        imageView2_1 = findViewById(R.id.imageView2_1);
        imageView2_2 = findViewById(R.id.imageView2_2);
        imageView2_3 = findViewById(R.id.imageView2_3);
        imageView3_1 = findViewById(R.id.imageView3_1);
        imageView3_2 = findViewById(R.id.imageView3_2);
        imageView3_3 = findViewById(R.id.imageView3_3);
        imageView4_1 = findViewById(R.id.imageView4_1);
        imageView4_2 = findViewById(R.id.imageView4_2);
        imageView4_3 = findViewById(R.id.imageView4_3);

        imageViewList.add(findViewById(R.id.imageView1_1));
        imageViewList.add(findViewById(R.id.imageView1_2));
        imageViewList.add(findViewById(R.id.imageView1_3));
        imageViewList.add(findViewById(R.id.imageView2_1));
        imageViewList.add(findViewById(R.id.imageView2_2));
        imageViewList.add(findViewById(R.id.imageView2_3));
        imageViewList.add(findViewById(R.id.imageView3_1));
        imageViewList.add(findViewById(R.id.imageView3_2));
        imageViewList.add(findViewById(R.id.imageView3_3));
        imageViewList.add(findViewById(R.id.imageView4_1));
        imageViewList.add(findViewById(R.id.imageView4_2));
        imageViewList.add(findViewById(R.id.imageView4_3));
    }

    private void locateImages(int imageCount) {
        if(imageCount == 2) {
            changeImageVisibility(View.GONE);
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
        } else if(imageCount == 4) {
            changeImageVisibility(View.GONE);
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
            imageView2_1.setVisibility(View.VISIBLE);
            imageView2_2.setVisibility(View.VISIBLE);
        } else if(imageCount == 6) {
            changeImageVisibility(View.GONE);
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
            imageView2_1.setVisibility(View.VISIBLE);
            imageView2_2.setVisibility(View.VISIBLE);
            imageView3_1.setVisibility(View.VISIBLE);
            imageView3_2.setVisibility(View.VISIBLE);
        } else if(imageCount == 8) {
            changeImageVisibility(View.GONE);
            imageView1_1.setVisibility(View.INVISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
            imageView1_3.setVisibility(View.INVISIBLE);
            imageView2_1.setVisibility(View.VISIBLE);
            imageView2_2.setVisibility(View.VISIBLE);
            imageView2_3.setVisibility(View.VISIBLE);
            imageView3_1.setVisibility(View.VISIBLE);
            imageView3_2.setVisibility(View.VISIBLE);
            imageView3_3.setVisibility(View.VISIBLE);
            imageView4_1.setVisibility(View.INVISIBLE);
            imageView4_2.setVisibility(View.VISIBLE);
            imageView4_3.setVisibility(View.INVISIBLE);
        } else if(imageCount == 12) {
            changeImageVisibility(View.VISIBLE);
        }
    }

    private void changeImageVisibility(int visibility) {
        for (ImageView imageView : imageViewList) {
            imageView.setVisibility(visibility);
        }
        Log.d("ChangeImageVisibility", "Tüm ImageView'ların görünürlüğü değiştirildi: " + visibility);
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
                Collections.sort(categoryInfoList, Comparator.comparingInt(CategoryInfo::getOrder));

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
        String difficultyString = "";
        if(difficultyEnum.name().equals("beginner")) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.darkOnTop);
            setStarColor(difficultyStarThree, R.color.darkOnTop);
            difficultyString = getString(R.string.exam_difficulty_beginner);
        } else if(difficultyEnum.name().equals("medium")) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.warningColor);
            setStarColor(difficultyStarThree, R.color.darkOnTop);
            difficultyString = getString(R.string.exam_difficulty_medium);
        } else if(difficultyEnum.name().equals("hard")) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.warningColor);
            setStarColor(difficultyStarThree, R.color.warningColor);
            difficultyString = getString(R.string.exam_difficulty_hard);
        }

        difficultyText.setText(difficultyString);
    }

    private void setStarColor(ImageView star, int colorResId) {
        int color = ContextCompat.getColor(this, colorResId);

        star.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    }
}
