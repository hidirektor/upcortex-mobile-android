package me.t3sl4.upcortex.Model.Exam;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Question implements Parcelable {
    private String id;
    private String preText;
    private String mainText;
    private String subText;
    private String fileName;
    private String point;
    private boolean isParent;
    private String difficulty;
    private String type;
    private String parentId;
    private List<QuestionOption> questionOptions;

    public Question() {

    }

    public Question(Parcel in) {
        id = in.readString();
        preText = in.readString();
        mainText = in.readString();
        subText = in.readString();
        fileName = in.readString();
        point = in.readString();
        isParent = in.readByte() != 0;
        difficulty = in.readString();
        type = in.readString();
        parentId = in.readString();
        questionOptions = in.createTypedArrayList(QuestionOption.CREATOR);
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(preText);
        dest.writeString(mainText);
        dest.writeString(subText);
        dest.writeString(fileName);
        dest.writeString(point);
        dest.writeByte((byte) (isParent ? 1 : 0));
        dest.writeString(difficulty);
        dest.writeString(type);
        dest.writeString(parentId);
        dest.writeTypedList(questionOptions);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreText() {
        return preText;
    }

    public void setPreText(String preText) {
        this.preText = preText;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<QuestionOption> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(List<QuestionOption> questionOptions) {
        this.questionOptions = questionOptions;
    }
}