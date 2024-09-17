package me.t3sl4.upcortex.Model.Exam;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ExamResponse implements Parcelable {
    private Exam exam;
    private List<QuestionCategory> questionsByCategories;

    public ExamResponse() {
        // Boş yapıcı
    }

    public ExamResponse(Parcel in) {  // Yapıcıyı public yapıyoruz
        exam = in.readParcelable(Exam.class.getClassLoader());
        questionsByCategories = in.createTypedArrayList(QuestionCategory.CREATOR);
    }

    public static final Creator<ExamResponse> CREATOR = new Creator<ExamResponse>() {
        @Override
        public ExamResponse createFromParcel(Parcel in) {
            return new ExamResponse(in);
        }

        @Override
        public ExamResponse[] newArray(int size) {
            return new ExamResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(exam, flags);
        dest.writeTypedList(questionsByCategories);
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public List<QuestionCategory> getQuestionsByCategories() {
        return questionsByCategories;
    }

    public void setQuestionsByCategories(List<QuestionCategory> questionsByCategories) {
        this.questionsByCategories = questionsByCategories;
    }
}