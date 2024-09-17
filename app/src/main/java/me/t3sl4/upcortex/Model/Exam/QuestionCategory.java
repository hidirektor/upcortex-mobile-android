package me.t3sl4.upcortex.Model.Exam;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class QuestionCategory implements Parcelable {
    private String id;
    private String name;
    private int order;
    private List<Question> questions;

    public QuestionCategory() {

    }

    public QuestionCategory(Parcel in) {
        id = in.readString();
        name = in.readString();
        order = in.readInt();
        questions = in.createTypedArrayList(Question.CREATOR);
    }

    public static final Creator<QuestionCategory> CREATOR = new Creator<QuestionCategory>() {
        @Override
        public QuestionCategory createFromParcel(Parcel in) {
            return new QuestionCategory(in);
        }

        @Override
        public QuestionCategory[] newArray(int size) {
            return new QuestionCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(order);
        dest.writeTypedList(questions);
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}