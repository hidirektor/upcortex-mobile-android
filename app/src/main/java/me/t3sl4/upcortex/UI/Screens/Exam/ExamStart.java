package me.t3sl4.upcortex.UI.Screens.Exam;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.R;

public class ExamStart extends AppCompatActivity {

    private Exam receivedExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_start);

        receivedExam = getIntent().getParcelableExtra("exam");

        initializeComponents();
        buttonClickListeners();
    }

    private void initializeComponents() {

    }

    private void buttonClickListeners() {

    }
}
