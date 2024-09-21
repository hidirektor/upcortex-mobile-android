package me.t3sl4.upcortex.Model.Exam.Question;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.t3sl4.upcortex.Model.Exam.Difficulty.Difficulty;

public class Question {
    private String id;
    private String preText;
    private String mainText;
    private String subText;
    private String fileName;
    private double point;
    private boolean isParent;
    private Difficulty difficulty;
    private QuestionType type;
    private String parentId;
    private String format;

    @SerializedName("questionOptions")
    private List<QuestionOption> questionOptions;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getPreText() {
        return preText;
    }

    public String getMainText() {
        return mainText;
    }

    public String getSubText() {
        return subText;
    }

    public String getFileName() {
        return fileName;
    }

    public double getPoint() {
        return point;
    }

    public boolean isParent() {
        return isParent;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public QuestionType getType() {
        return type;
    }

    public String getParentId() {
        return parentId;
    }

    public String getFormat() {
        return format;
    }

    public List<QuestionOption> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(List<QuestionOption> questionOptions) {
        this.questionOptions = questionOptions;
    }
}