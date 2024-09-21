package me.t3sl4.upcortex.Model.Exam.Question;

public class QuestionOption {
    private String id;
    private String text;
    private boolean isCorrect;
    private QuestionType type;
    private String fileName;
    private int order;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public QuestionType getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public int getOrder() {
        return order;
    }
}