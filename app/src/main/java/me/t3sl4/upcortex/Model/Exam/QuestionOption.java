package me.t3sl4.upcortex.Model.Exam;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionOption implements Parcelable {
    private String id;
    private String text;
    private boolean isCorrect;
    private String type;
    private String fileName;
    private int order;

    public QuestionOption() {

    }

    public QuestionOption(Parcel in) {
        id = in.readString();
        text = in.readString();
        isCorrect = in.readByte() != 0;
        type = in.readString();
        fileName = in.readString();
        order = in.readInt();
    }

    public static final Creator<QuestionOption> CREATOR = new Creator<QuestionOption>() {
        @Override
        public QuestionOption createFromParcel(Parcel in) {
            return new QuestionOption(in);
        }

        @Override
        public QuestionOption[] newArray(int size) {
            return new QuestionOption[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(text);
        dest.writeByte((byte) (isCorrect ? 1 : 0));
        dest.writeString(type);
        dest.writeString(fileName);
        dest.writeInt(order);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}