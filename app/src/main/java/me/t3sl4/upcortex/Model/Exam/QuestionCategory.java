package me.t3sl4.upcortex.Model.Exam;

import java.util.LinkedList;

public class QuestionCategory {

    private String id;
    private String name;
    private int order;
    private LinkedList<CategoryClassification> categoryClassifications;

    public QuestionCategory(String id, String name, int order, LinkedList<CategoryClassification> categoryClassifications) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.categoryClassifications = categoryClassifications;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public LinkedList<CategoryClassification> getCategoryClassifications() {
        return categoryClassifications;
    }

    public void setCategoryClassifications(LinkedList<CategoryClassification> categoryClassifications) {
        this.categoryClassifications = categoryClassifications;
    }
}
