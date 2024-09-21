package me.t3sl4.upcortex.Model.Exam;

public class GeneralClassification {
    private String id;
    private String name;
    private int minVal;
    private int maxVal;

    public GeneralClassification(String id, String name, int minVal, int maxVal) {
        this.id = id;
        this.name = name;
        this.minVal = minVal;
        this.maxVal = maxVal;
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
}
