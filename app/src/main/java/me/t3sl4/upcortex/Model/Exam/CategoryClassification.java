package me.t3sl4.upcortex.Model.Exam;

public class CategoryClassification {
    private String id;
    private String name;
    private int minVal;
    private int maxVal;
    private String description;

    public CategoryClassification(String id, String name, int minVal, int maxVal, String description) {
        this.id = id;
        this.name = name;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.description = description;
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

    public int getMinVal() {
        return minVal;
    }

    public void setMinVal(int minVal) {
        this.minVal = minVal;
    }

    public int getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
