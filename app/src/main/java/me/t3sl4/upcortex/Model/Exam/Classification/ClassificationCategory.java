package me.t3sl4.upcortex.Model.Exam.Classification;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassificationCategory {
    private String id;
    private String name;
    private int order;

    @SerializedName("subCategories")
    private List<Classification> subCategories;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public List<Classification> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Classification> subCategories) {
        this.subCategories = subCategories;
    }
}
