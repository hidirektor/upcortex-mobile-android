package me.t3sl4.upcortex.Model.Exam.Question;

import java.util.List;

import me.t3sl4.upcortex.Model.Exam.SubCategory.CategoryName;

public class QuestionsByCategory {
    private String id;
    private String name;
    private int order;
    private List<Question> questions;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public CategoryName getCategoryName() {
        return CategoryName.fromString(name);
    }

    public void setCategoryName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}