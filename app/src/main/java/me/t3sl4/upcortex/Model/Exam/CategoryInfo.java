package me.t3sl4.upcortex.Model.Exam;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryInfo implements Parcelable {
    private String name;
    private int order;
    private int questionCount;
    private int userPoint;

    public CategoryInfo(String name, int order) {
        this.name = name;
        this.order = order;
        this.questionCount = 0;
        this.userPoint = 0;
    }

    protected CategoryInfo(Parcel in) {
        name = in.readString();
        order = in.readInt();
        questionCount = in.readInt();
        userPoint = in.readInt();
    }

    public static final Creator<CategoryInfo> CREATOR = new Creator<CategoryInfo>() {
        @Override
        public CategoryInfo createFromParcel(Parcel in) {
            return new CategoryInfo(in);
        }

        @Override
        public CategoryInfo[] newArray(int size) {
            return new CategoryInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeInt(order);
        parcel.writeInt(questionCount);
        parcel.writeInt(userPoint);
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

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }

    public void incrementQuestionCount() {
        this.questionCount++;
    }

    public void addUserPoint(int points) {
        this.userPoint += points;
    }
}