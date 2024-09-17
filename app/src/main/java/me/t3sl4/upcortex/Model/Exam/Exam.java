package me.t3sl4.upcortex.Model.Exam;

import android.os.Parcel;
import android.os.Parcelable;

public class Exam implements Parcelable {
    private String id;
    private String name;
    private boolean approvalState;
    private int scale;
    private int max;
    private boolean isDefault;
    private String examState;

    public Exam() {

    }

    public Exam(Parcel in) {
        id = in.readString();
        name = in.readString();
        approvalState = in.readByte() != 0;
        scale = in.readInt();
        max = in.readInt();
        isDefault = in.readByte() != 0;
        examState = in.readString();
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeByte((byte) (approvalState ? 1 : 0));
        dest.writeInt(scale);
        dest.writeInt(max);
        dest.writeByte((byte) (isDefault ? 1 : 0));
        dest.writeString(examState);
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

    public boolean isApprovalState() {
        return approvalState;
    }

    public void setApprovalState(boolean approvalState) {
        this.approvalState = approvalState;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getExamState() {
        return examState;
    }

    public void setExamState(String examState) {
        this.examState = examState;
    }
}