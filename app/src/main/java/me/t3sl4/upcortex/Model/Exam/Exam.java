package me.t3sl4.upcortex.Model.Exam;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Exam implements Parcelable {

    private String examID;
    private String examName;
    private boolean approvalState;
    private int examScale;
    private int examMax;
    private boolean isDefault;
    private String examCreatedBy;
    private String examApprovedBy;
    private ExamState userExamState;
    private int examTime;
    private String examDescription;
    private String examInstructions;
    private String beforeText;
    private float userPoint;
    private LinkedList<QuestionCategory> questionCategories;
    private LinkedList<Question> questions;
    private LinkedList<GeneralClassification> examClassifications;
    private List<CategoryInfo> categoryInfoList;

    public Exam(String examID, String examName, boolean approvalState, int examScale, int examMax, boolean isDefault, String examCreatedBy, String examApprovedBy, ExamState userExamState, int examTime, String examDescription, String examInstructions, String beforeText, int userPoint) {
        this.examID = examID;
        this.examName = examName;
        this.approvalState = approvalState;
        this.examScale = examScale;
        this.examMax = examMax;
        this.isDefault = isDefault;
        this.examCreatedBy = examCreatedBy;
        this.examApprovedBy = examApprovedBy;
        this.userExamState = userExamState;
        this.examTime = examTime;
        this.examDescription = examDescription;
        this.examInstructions = examInstructions;
        this.beforeText = beforeText;
        this.userPoint = userPoint;
        this.categoryInfoList = new ArrayList<>();
    }

    protected Exam(Parcel in) {
        examID = in.readString();
        examName = in.readString();
        approvalState = in.readByte() != 0;
        examScale = in.readInt();
        examMax = in.readInt();
        isDefault = in.readByte() != 0;
        examCreatedBy = in.readString();
        examApprovedBy = in.readString();
        userExamState = ExamState.valueOf(in.readString()); // Assuming ExamState is an Enum
        examTime = in.readInt();
        examDescription = in.readString();
        examInstructions = in.readString();
        beforeText = in.readString();
        userPoint = in.readInt();
    }

    public static final Creator<Exam> CREATOR = new Creator<Exam>() {
        @Override
        public Exam createFromParcel(Parcel in) {
            return new Exam(in);
        }

        @Override
        public Exam[] newArray(int size) {
            return new Exam[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(examID);
        parcel.writeString(examName);
        parcel.writeByte((byte) (approvalState ? 1 : 0));
        parcel.writeInt(examScale);
        parcel.writeInt(examMax);
        parcel.writeByte((byte) (isDefault ? 1 : 0));
        parcel.writeString(examCreatedBy);
        parcel.writeString(examApprovedBy);
        parcel.writeString(userExamState.name());
        parcel.writeInt(examTime);
        parcel.writeString(examDescription);
        parcel.writeString(examInstructions);
        parcel.writeString(beforeText);
        parcel.writeFloat(userPoint);
    }

    public String getExamID() {
        return examID;
    }

    public void setExamID(String examID) {
        this.examID = examID;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public boolean isApprovalState() {
        return approvalState;
    }

    public void setApprovalState(boolean approvalState) {
        this.approvalState = approvalState;
    }

    public int getExamScale() {
        return examScale;
    }

    public void setExamScale(int examScale) {
        this.examScale = examScale;
    }

    public int getExamMax() {
        return examMax;
    }

    public void setExamMax(int examMax) {
        this.examMax = examMax;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getExamCreatedBy() {
        return examCreatedBy;
    }

    public void setExamCreatedBy(String examCreatedBy) {
        this.examCreatedBy = examCreatedBy;
    }

    public String getExamApprovedBy() {
        return examApprovedBy;
    }

    public void setExamApprovedBy(String examApprovedBy) {
        this.examApprovedBy = examApprovedBy;
    }

    public ExamState getUserExamState() {
        return userExamState;
    }

    public void setUserExamState(ExamState userExamState) {
        this.userExamState = userExamState;
    }

    public int getExamTime() {
        return examTime;
    }

    public void setExamTime(int examTime) {
        this.examTime = examTime;
    }

    public String getExamDescription() {
        return examDescription;
    }

    public void setExamDescription(String examDescription) {
        this.examDescription = examDescription;
    }

    public String getExamInstructions() {
        return examInstructions;
    }

    public void setExamInstructions(String examInstructions) {
        this.examInstructions = examInstructions;
    }

    public String getBeforeText() {
        return beforeText;
    }

    public void setBeforeText(String beforeText) {
        this.beforeText = beforeText;
    }

    public float getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(float userPoint) {
        this.userPoint = userPoint;
    }

    public LinkedList<QuestionCategory> getQuestionCategories() {
        return questionCategories;
    }

    public void setQuestionCategories(LinkedList<QuestionCategory> questionCategories) {
        this.questionCategories = questionCategories;
    }

    public LinkedList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(LinkedList<Question> questions) {
        this.questions = questions;
    }

    public LinkedList<GeneralClassification> getExamClassifications() {
        return examClassifications;
    }

    public void setExamClassifications(LinkedList<GeneralClassification> examClassifications) {
        this.examClassifications = examClassifications;
    }

    public List<CategoryInfo> getCategoryInfoList() {
        return categoryInfoList;
    }

    public void setCategoryInfoList(List<CategoryInfo> categoryInfoList) {
        this.categoryInfoList = categoryInfoList;
    }
}