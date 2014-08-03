
package org.missionassetfund.apps.android.models;

public enum PostType {
    MESSAGE(1, "Message"),
    QUESTION(2, "Question"),
    EVENT(3, "Event"),
    REMINDER(4, "Reminder");

    private int typeValue;
    private String prettyName;

    private PostType(int typeValue) {
        this.typeValue = typeValue;
    }

    private PostType(int typeValue, String prettyName) {
        this.typeValue = typeValue;
        this.prettyName = prettyName;
    }

    private PostType(String prettyName) {
        this.prettyName = prettyName;
    }

    public int toInt() {
        return typeValue;
    }

    public static PostType getTypeFromInt(int typeValue) {
        switch (typeValue) {
            case 1:
                return MESSAGE;
            case 2:
                return QUESTION;
            case 3:
                return EVENT;
            case 4:
                return REMINDER;
            default:
                return MESSAGE;
        }
    }

    @Override
    public String toString() {
        return prettyName;
    }
}
