package me.t3sl4.upcortex.UI.Screens.Exam;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import me.t3sl4.upcortex.Model.Exam.Difficulty.Difficulty;
import me.t3sl4.upcortex.R;

public class QuestionProcess extends AppCompatActivity {

    //DifficultyStars
    private ImageView difficultyStarOne;
    private ImageView difficultyStarTwo;
    private ImageView difficultyStarThree;

    private int questionTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_process);

        initializeComponents();
    }

    private void initializeComponents() {
        difficultyStarOne = findViewById(R.id.difficultyStarOne);
        difficultyStarTwo = findViewById(R.id.difficultyStarTwo);
        difficultyStarThree = findViewById(R.id.difficultyStarThree);
    }

    private void difficultyMode(Difficulty difficultyEnum) {
        if(difficultyEnum.getValue().equals("beginner")) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.darkOnTop);
            setStarColor(difficultyStarThree, R.color.darkOnTop);
        } else if(difficultyEnum.getValue().equals("medium")) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.warningColor);
            setStarColor(difficultyStarThree, R.color.darkOnTop);
        } else if(difficultyEnum.getValue().equals("hard")) {
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
