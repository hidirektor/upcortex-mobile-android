package me.t3sl4.upcortex.Model.Exam.Difficulty;

public enum Difficulty {
    BEGINNER("beginner"),
    MEDIUM("medium"),
    HARD("hard");

    private final String value;

    Difficulty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Difficulty fromString(String text) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.value.equalsIgnoreCase(text)) {
                return difficulty;
            }
        }
        throw new IllegalArgumentException("Unknown difficulty: " + text);
    }
}
