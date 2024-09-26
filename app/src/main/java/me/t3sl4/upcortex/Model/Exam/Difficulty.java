package me.t3sl4.upcortex.Model.Exam;

import com.google.gson.annotations.SerializedName;

public enum Difficulty {
    @SerializedName("beginner")
    BEGINNER,

    @SerializedName("medium")
    MEDIUM,

    @SerializedName("hard")
    HARD,

    @SerializedName("none")
    NONE
}