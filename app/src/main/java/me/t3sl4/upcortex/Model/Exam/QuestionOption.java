package me.t3sl4.upcortex.Model.Exam;

public class QuestionOption {
    private String id;
    private String text;
    private boolean isCorrect;
    private String type;
    private String fileName;
    private int order;

    public QuestionOption(String id, String text, boolean isCorrect, String type, String fileName, int order) {
        this.id = id;
        this.text = text;
        this.isCorrect = isCorrect;
        this.type = type;
        this.fileName = fileName;
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
