package me.t3sl4.upcortex.Model.Exam.Question;

public enum QuestionType {
    TEXT("text"),
    IMAGE("image");

    private final String value;

    QuestionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static QuestionType fromString(String text) {
        for (QuestionType type : QuestionType.values()) {
            if (type.value.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown question type: " + text);
    }
}