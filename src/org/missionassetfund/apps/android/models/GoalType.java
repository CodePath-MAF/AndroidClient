
package org.missionassetfund.apps.android.models;

public enum GoalType {
    LENDING_CIRCLE(1, "Lending Circle"),
    GENERAL(2, "General");

    private int typeValue;
    private String prettyName;

    private GoalType(int typeValue) {
        this.typeValue = typeValue;
    }

    private GoalType(int typeValue, String prettyName) {
        this.typeValue = typeValue;
        this.prettyName = prettyName;
    }

    private GoalType(String prettyName) {
        this.prettyName = prettyName;
    }

    public int toInt() {
        return typeValue;
    }

    public static GoalType getTypeFromInt(int typeValue) {
        switch (typeValue) {
            case 1:
                return LENDING_CIRCLE;
            default:
                return GENERAL;
        }
    }

    @Override
    public String toString() {
        return prettyName;
    }
}
