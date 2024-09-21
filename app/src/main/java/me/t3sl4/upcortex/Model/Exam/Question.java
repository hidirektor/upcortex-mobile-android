package me.t3sl4.upcortex.Model.Exam;

import java.util.LinkedList;

public class Question {
    private String categoryName;

    private String id;
    private String preText;
    private String mainText;
    private String subText;
    private String fileName;
    private float point;
    private boolean isParent;
    private Difficulty difficulty;
    private QuestionType type;
    private String parentId;
    private String format;
    private int correctOptionsCount;
    private int totalOptionsCount;
    private LinkedList<QuestionOption> questionOptions;

    public Question(String categoryName, String id, String preText, String mainText, String subText, String fileName, float point, boolean isParent, Difficulty difficulty, QuestionType type, String parentId, String format, int correctOptionsCount, int totalOptionsCount, LinkedList<QuestionOption> questionOptions) {
        this.categoryName = categoryName;
        this.id = id;
        this.preText = preText;
        this.mainText = mainText;
        this.subText = subText;
        this.fileName = fileName;
        this.point = point;
        this.isParent = isParent;
        this.difficulty = difficulty;
        this.type = type;
        this.parentId = parentId;
        this.format = format;
        this.correctOptionsCount = correctOptionsCount;
        this.totalOptionsCount = totalOptionsCount;
        this.questionOptions = questionOptions;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreText() {
        return preText;
    }

    public void setPreText(String preText) {
        this.preText = preText;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getCorrectOptionsCount() {
        return correctOptionsCount;
    }

    public void setCorrectOptionsCount(int correctOptionsCount) {
        this.correctOptionsCount = correctOptionsCount;
    }

    public int getTotalOptionsCount() {
        return totalOptionsCount;
    }

    public void setTotalOptionsCount(int totalOptionsCount) {
        this.totalOptionsCount = totalOptionsCount;
    }

    public LinkedList<QuestionOption> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(LinkedList<QuestionOption> questionOptions) {
        this.questionOptions = questionOptions;
    }
}
