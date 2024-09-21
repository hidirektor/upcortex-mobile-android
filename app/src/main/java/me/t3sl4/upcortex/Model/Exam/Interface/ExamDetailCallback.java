package me.t3sl4.upcortex.Model.Exam.Interface;

import me.t3sl4.upcortex.Model.Exam.ExamDetail.ExamDetailResponse;

public interface ExamDetailCallback {
    void onSuccess(ExamDetailResponse examDetail);
    void onFailure(String errorMessage);
}