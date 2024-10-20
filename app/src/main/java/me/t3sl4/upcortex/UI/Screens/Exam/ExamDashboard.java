package me.t3sl4.upcortex.UI.Screens.Exam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.t3sl4.upcortex.Model.Exam.Adapter.ExamResultAdapter;
import me.t3sl4.upcortex.Model.Exam.CategoryClassification;
import me.t3sl4.upcortex.Model.Exam.CategoryInfo;
import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.Model.Exam.GeneralClassification;
import me.t3sl4.upcortex.Model.Exam.QuestionCategory;
import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.CircularStats.Speedometer;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Utils.HTTP.Requests.Exam.ExamService;
import me.t3sl4.upcortex.Utils.Screen.ScreenUtil;

public class ExamDashboard extends AppCompatActivity {
    private static final int EXAM_REQUEST_CODE = 1;

    private Exam receivedExam;

    private Button startButton;
    private Button backButton;
    private Button closeExamButton;

    private TextView generalExamResult;
    private Button currentExamPercent;
    private Speedometer currentExamGeneralStat;
    private TextView generalExamDesc;
    private TextView generalExamDiagnose;

    private LinearLayout examStartLayout;
    private LinearLayout examStatsLayout;

    private RecyclerView examDetailsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_dashboard);

        receivedExam = getIntent().getParcelableExtra("exam");

        ScreenUtil.hideNavAndStatus(ExamDashboard.this);
        ScreenUtil.fullScreenMode(ExamDashboard.this);

        initializeComponents();

        buttonClickListeners();

        loadExamDetails();
    }

    private void initializeComponents() {
        startButton = findViewById(R.id.startButton);
        backButton = findViewById(R.id.backButton);
        closeExamButton = findViewById(R.id.closeExamButton);

        examStartLayout = findViewById(R.id.examStartLayout);
        examStatsLayout = findViewById(R.id.examStatsLayout);

        examDetailsCard = findViewById(R.id.examDetailsCard);

        generalExamResult = findViewById(R.id.generalExamResult);
        currentExamGeneralStat = findViewById(R.id.currentExamGeneralStat);
        currentExamPercent = findViewById(R.id.currentExamPercent);
        generalExamDesc = findViewById(R.id.generalExamDesc);
        generalExamDiagnose = findViewById(R.id.generalExamDiagnose);

        if(receivedExam.getUserPoint() != 0) {
            examStartLayout.setVisibility(View.GONE);
            startButton.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            examStatsLayout.setVisibility(View.VISIBLE);
            closeExamButton.setVisibility(View.VISIBLE);
        } else {
            examStatsLayout.setVisibility(View.GONE);
            closeExamButton.setVisibility(View.GONE);
            examStartLayout.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        }
    }

    private void buttonClickListeners() {
        backButton.setOnClickListener(v -> {
            finish();
        });

        startButton.setOnClickListener(v -> {
            startExamProcess();
        });

        closeExamButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadExamDetails() {
        if(receivedExam != null) {
            ExamService.getExamDetail(ExamDashboard.this, () -> {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String examJson = gson.toJson(receivedExam);

                //Log.d("Exam Details", examJson);
            }, () -> {
                Sneaker.with(ExamDashboard.this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.exam_detail_cant_retrieve))
                        .sneakError();
            }, receivedExam.getExamID(), receivedExam);
        }
    }

    private void loadExamStats(HashMap<String, Float> categoryPoints, float generalPoint, Exam currentExam) {
        List<String> categoryList = new ArrayList<>();
        List<Float> percentList = new ArrayList<>();

        // 1. Genel Sınıflandırmayı Bulma
        String generalName = "Bilinmiyor";
        String generalDesc = "Açıklama mevcut değil.";
        for (GeneralClassification gc : currentExam.getExamClassifications()) {
            if (generalPoint >= gc.getMinVal() && generalPoint <= gc.getMaxVal()) {
                generalName = gc.getName();
                generalDesc = gc.getDescription();
                break;
            }
        }

        currentExamPercent.setText("% " + generalPoint);
        generalExamResult.setText(generalName);
        generalExamDesc.setText(generalDesc);
        //currentExamGeneralStat.setPercent((int) generalPoint, 2000L, () -> {
            //return null;
        //});
        currentExamGeneralStat.setVisibility(View.GONE);
        generalExamDiagnose.setVisibility(View.GONE);

        // 2. Kategori Sınıflandırmalarını Bulma
        List<String> subNameList = new ArrayList<>();
        List<String> subDescList = new ArrayList<>();

        for (Map.Entry<String, Float> entry : categoryPoints.entrySet()) {
            String categoryName = entry.getKey();
            float categoryPoint = entry.getValue();

            categoryList.add(categoryName);
            percentList.add(categoryPoint);

            // İlgili Kategoriyi Bulma
            QuestionCategory matchedCategory = null;
            for (QuestionCategory qc : currentExam.getQuestionCategories()) {
                if (qc.getName().equalsIgnoreCase(categoryName)) {
                    matchedCategory = qc;
                    break;
                }
            }

            if (matchedCategory != null) {
                // Kategoriye Ait Sınıflandırmaları Dolaşma
                boolean classificationFound = false;
                for (CategoryClassification cc : matchedCategory.getCategoryClassifications()) {
                    Log.d("Kategory Point", categoryPoint + " ");
                    Log.d("Kategori Stats", cc.getName() + " " + cc.getDescription() + " " + cc.getMinVal() + " " + cc.getMaxVal());
                    if (categoryPoint >= Float.valueOf(cc.getMinVal()) && categoryPoint <= Float.valueOf(cc.getMaxVal())) {
                        subNameList.add(cc.getName());
                        subDescList.add(cc.getDescription());
                        classificationFound = true;
                        break;
                    }
                }
                if (!classificationFound) {
                    subNameList.add("Sınıflandırma Bulunamadı");
                    subDescList.add("Bu kategori için uygun bir sınıflandırma bulunamadı.");
                }
            } else {
                subNameList.add("Kategori Bulunamadı");
                subDescList.add("Bu kategoriye ait veriler mevcut değil.");
            }
        }

        // Adapter ve Layout Manager Ayarlanması
        ExamResultAdapter examResultAdapter = new ExamResultAdapter(categoryList, subNameList, subDescList, percentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        examDetailsCard.setLayoutManager(layoutManager);
        examDetailsCard.setAdapter(examResultAdapter);
    }

    private void startExamProcess() {
        Intent examIntent = new Intent(ExamDashboard.this, ExamProcess.class);
        Gson gson = new Gson();
        String examJson = gson.toJson(receivedExam);

        examIntent.putExtra("examJson", examJson);
        startActivityForResult(examIntent, EXAM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXAM_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String updatedCategoryListJson = data.getStringExtra("updatedCategoryListJson");
                if (updatedCategoryListJson != null) {
                    HashMap<String, Float> categoryPoints = new HashMap<>();
                    float generalPoint = 0;

                    Gson gson = new Gson();
                    if(parseFloatFromJson(updatedCategoryListJson) != null) {
                        generalPoint = parseFloatFromJson(updatedCategoryListJson);
                    } else {
                        CategoryInfo[] updatedCategoryList = gson.fromJson(updatedCategoryListJson, CategoryInfo[].class);

                        // Update the general point and category points from the list
                        for (CategoryInfo currentInfo : updatedCategoryList) {
                            categoryPoints.put(currentInfo.getName(), currentInfo.getUserPoint());
                            generalPoint += currentInfo.getUserPoint(); // Sum up the points for a general score
                        }
                    }

                    loadExamStats(categoryPoints, generalPoint, receivedExam); // Assuming loadExamStats uses the category points and general point
                    examStartLayout.setVisibility(View.GONE);
                    startButton.setVisibility(View.GONE);
                    backButton.setVisibility(View.GONE);
                    examStatsLayout.setVisibility(View.VISIBLE);
                    closeExamButton.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public static Float parseFloatFromJson(String jsonString) {
        Gson gson = new Gson();
        try {
            // JSON stringini bir JsonElement'e çevir
            JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);

            // Eğer JsonElement bir sayıysa ve float'a dönüştürülebiliyorsa döndür
            if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber()) {
                return jsonElement.getAsFloat();
            }
        } catch (JsonSyntaxException e) {
            // JSON formatı hatalıysa
            e.printStackTrace();
        }

        // Eğer float bir sayı bulunmazsa veya format hatalıysa null döner
        return null;
    }
}
