
package org.missionassetfund.apps.android.models;


public enum GoalPaymentInterval {
    UNDEFINED(0),
    DAILY(1),
    WEEKLY(7),
    BIWEEKLY(14),
    MONTHLY(30),
    BIMONTHLY(60);

    private int intervalValue;

    private GoalPaymentInterval(int intervalValue) {
        this.intervalValue = intervalValue;
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
                return UNDEFINED;
        }
    }
}
