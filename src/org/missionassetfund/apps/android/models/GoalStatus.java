
package org.missionassetfund.apps.android.models;


public enum GoalStatus {
    UNDEFINED(0),
    IN_PROGRESS(1),
    ACHIEVED(2);

    private int statusValue;

    private GoalStatus(int statusValue) {
        this.statusValue = statusValue;
    }

    public int toInt() {
        return statusValue;
    }

    public static GoalStatus getTypeFromInt(int statusValue) {
        switch (statusValue) {
            case 1:
                return IN_PROGRESS;
            case 2:
                return ACHIEVED;
            default:
                return UNDEFINED;
        }
    }
}
