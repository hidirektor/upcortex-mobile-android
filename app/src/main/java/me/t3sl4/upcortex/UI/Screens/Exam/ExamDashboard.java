package me.t3sl4.upcortex.UI.Screens.Exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import me.t3sl4.upcortex.Model.Exam.Adapter.ExamResultAdapter;
import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Utility.HTTP.Requests.Exam.ExamService;
import me.t3sl4.upcortex.Utility.Screen.ScreenUtil;

public class ExamDashboard extends AppCompatActivity {

    private Exam receivedExam;

    private Button startButton;
    private Button backButton;
    private Button closeExamButton;

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

        if(receivedExam.getUserPoint() != 0) {
            loadTempDetails();
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

    private void loadTempDetails() {
        // Örnek String ve Integer verileri
        List<String> categoryList = Arrays.asList("Kısa Süreli Bellek", "Uzun Süreli Bellek", "Görsel Bellek", "İşlemsel Bellek");
        List<String> subNameList = Arrays.asList("Çok İyi Seviye", "İyi Seviye", "Yüksek Tahribat", "Düşük Seviye");
        List<String> subDescList = Arrays.asList("Görsel bellek yetenekleri mükemmeldir. Görsel bilgileri hızlı bir şekilde öğrenir ve hatırlarlar. Bu, mekan tanıma ve yön bulma gibi günlük görevlerde büyük bir avantaj sağlar.", "Görsel bellek yetenekleri mükemmeldir. Görsel bilgileri hızlı bir şekilde öğrenir ve hatırlarlar. Bu, mekan tanıma ve yön bulma gibi günlük görevlerde büyük bir avantaj sağlar.", "Görsel bellek yetenekleri mükemmeldir. Görsel bilgileri hızlı bir şekilde öğrenir ve hatırlarlar. Bu, mekan tanıma ve yön bulma gibi günlük görevlerde büyük bir avantaj sağlar.", "Görsel bellek yetenekleri mükemmeldir. Görsel bilgileri hızlı bir şekilde öğrenir ve hatırlarlar. Bu, mekan tanıma ve yön bulma gibi günlük görevlerde büyük bir avantaj sağlar.");
        List<Integer> percentList = Arrays.asList(75, 60, 90, 45);

        // Adapter ve layout manager ayarlanması
        ExamResultAdapter examResultAdapter = new ExamResultAdapter(categoryList, subNameList, subDescList, percentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        examDetailsCard.setLayoutManager(layoutManager);
        examDetailsCard.setAdapter(examResultAdapter);
    }

    private void startExamProcess() {
        Intent examIntent = new Intent(ExamDashboard.this, ExamProcess.class);
        examIntent.putExtra("exam", receivedExam);
        startActivity(examIntent);
    }
}
