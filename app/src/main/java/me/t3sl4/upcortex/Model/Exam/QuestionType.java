package me.t3sl4.upcortex.Model.Exam;

import com.google.gson.annotations.SerializedName;

public enum QuestionType {
    @SerializedName("text")
    TEXT,

    @SerializedName("image")
    IMAGE
}