package me.t3sl4.upcortex.Model.Exam.ExamDetail;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.t3sl4.upcortex.Model.Exam.Classification.Classification;
import me.t3sl4.upcortex.Model.Exam.Classification.ClassificationCategory;
import me.t3sl4.upcortex.Model.Exam.Question.QuestionsByCategory;

public class ExamDetailResponse {
    @SerializedName("questionsByCategories")
    private List<QuestionsByCategory> questionsByCategories;

    @SerializedName("questionCategoriesClassification")
    private List<ClassificationCategory> questionCategoriesClassification;

    @SerializedName("generalClassification")
    private List<Classification> generalClassification;

    // Getters and Setters
    public List<QuestionsByCategory> getQuestionsByCategories() {
        return questionsByCategories;
    }

    public void setQuestionsByCategories(List<QuestionsByCategory> questionsByCategories) {
        this.questionsByCategories = questionsByCategories;
    }

    public List<ClassificationCategory> getQuestionCategoriesClassification() {
        return questionCategoriesClassification;
    }

    public void setQuestionCategoriesClassification(List<ClassificationCategory> questionCategoriesClassification) {
        this.questionCategoriesClassification = questionCategoriesClassification;
    }

    public List<Classification> getGeneralClassification() {
        return generalClassification;
    }

    public void setGeneralClassification(List<Classification> generalClassification) {
        this.generalClassification = generalClassification;
    }
}
