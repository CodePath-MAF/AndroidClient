
package org.missionassetfund.apps.android.models;

public enum GoalPaymentInterval {
    // We do not need undefined. We will use Daily as default
    // UNDEFINED(0, "Undefined"),
    DAILY(1, "Daily"),
    WEEKLY(7, "Weekly"),
    BIWEEKLY(14, "Bi-weekly"),
    MONTHLY(30, "Monthly"),
    BIMONTHLY(60, "Bi-monthly");

    private int intervalValue;
    private String prettyName;

    private GoalPaymentInterval(int intervalValue) {
        this.intervalValue = intervalValue;
    }

    private GoalPaymentInterval(int intervalValue, String prettyName) {
        this.intervalValue = intervalValue;
        this.prettyName = prettyName;
    }

    private GoalPaymentInterval(String prettyName) {
        this.prettyName = prettyName;
    }

    public int toInt() {
        return intervalValue;
    }

    public static GoalPaymentInterval getTypeFromInt(int intervalValue) {
        switch (intervalValue) {
            case 1:
                return DAILY;
            case 7:
                return WEEKLY;
            case 14:
                return BIWEEKLY;
            case 30:
                return MONTHLY;
            case 60:
                return BIMONTHLY;
            default:
                return DAILY;
        }
    }

    @Override
    public String toString() {
        return prettyName;
    }
}
