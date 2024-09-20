package me.t3sl4.upcortex.UI.Screens.Exam;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.Utility.HTTP.Requests.Exam.ExamService;

public class ExamStart extends AppCompatActivity {

    private Exam receivedExam;

    private Button startButton;
    private Button backButton;

    private LinearLayout examStartLayout;
    private LinearLayout examStatsLayout;

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
            ExamService.getExamDetail(ExamStart.this, () -> {
                //Exam datası alındı
            }, () -> {
                //Exam datası alınamadı
            }, receivedExam.getExamID());
        }
    }

    private void startExamProcess() {

    }
}
