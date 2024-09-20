package me.t3sl4.upcortex.UI.Screens.Exam;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.Model.ExamResult.Adapter;
import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.Utility.HTTP.Requests.Exam.ExamService;

public class ExamProcess extends AppCompatActivity {

    private Exam receivedExam;

    private Button startButton;
    private Button backButton;

    private LinearLayout examStartLayout;
    private LinearLayout examStatsLayout;

    private RecyclerView examDetailsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_start);

        receivedExam = getIntent().getParcelableExtra("exam");

        initializeComponents();
        buttonClickListeners();

        loadExamDetails();
    }

    private void initializeComponents() {
        startButton = findViewById(R.id.startButton);
        backButton = findViewById(R.id.backButton);

        examStartLayout = findViewById(R.id.examStartLayout);
        examStartLayout.setVisibility(View.GONE);

        examDetailsCard = findViewById(R.id.examDetailsCard);
        loadTempDetails();
    }

    private void buttonClickListeners() {
        backButton.setOnClickListener(v -> {
            finish();
        });

        startButton.setOnClickListener(v -> {
            startExamProcess();
        });
    }

    private void loadExamDetails() {
        if(receivedExam != null) {
            ExamService.getExamDetail(ExamProcess.this, () -> {
                //Exam datası alındı
            }, () -> {
                //Exam datası alınamadı
            }, receivedExam.getExamID());
        }
    }

    private void loadTempDetails() {
        // Örnek String ve Integer verileri
        List<String> categoryList = Arrays.asList("Kısa Süreli Bellek", "Uzun Süreli Bellek", "Görsel Bellek", "İşlemsel Bellek");
        List<String> subNameList = Arrays.asList("Çok İyi Seviye", "İyi Seviye", "Yüksek Tahribat", "Düşük Seviye");
        List<String> subDescList = Arrays.asList("Görsel bellek yetenekleri mükemmeldir. Görsel bilgileri hızlı bir şekilde öğrenir ve hatırlarlar. Bu, mekan tanıma ve yön bulma gibi günlük görevlerde büyük bir avantaj sağlar.", "Görsel bellek yetenekleri mükemmeldir. Görsel bilgileri hızlı bir şekilde öğrenir ve hatırlarlar. Bu, mekan tanıma ve yön bulma gibi günlük görevlerde büyük bir avantaj sağlar.", "Görsel bellek yetenekleri mükemmeldir. Görsel bilgileri hızlı bir şekilde öğrenir ve hatırlarlar. Bu, mekan tanıma ve yön bulma gibi günlük görevlerde büyük bir avantaj sağlar.", "Görsel bellek yetenekleri mükemmeldir. Görsel bilgileri hızlı bir şekilde öğrenir ve hatırlarlar. Bu, mekan tanıma ve yön bulma gibi günlük görevlerde büyük bir avantaj sağlar.");
        List<Integer> percentList = Arrays.asList(75, 60, 90, 45);

        // Adapter ve layout manager ayarlanması
        Adapter adapter = new Adapter(categoryList, subNameList, subDescList, percentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        examDetailsCard.setLayoutManager(layoutManager);
        examDetailsCard.setAdapter(adapter);
    }

    private void startExamProcess() {

    }
}
