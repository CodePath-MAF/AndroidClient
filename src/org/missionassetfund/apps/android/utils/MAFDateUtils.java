
package org.missionassetfund.apps.android.utils;

import java.util.Date;

public class MAFDateUtils {
    private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    public static int getDaysSince(Date date) {
        return (int) ((getCurrentEpochTime() - date.getTime()) / ONE_DAY_IN_MILLIS);
    }

    private static long getCurrentEpochTime() {
        return new Date().getTime();
    }

    public static int getDaysTo(Date date) {
        return (int) ((date.getTime() - getCurrentEpochTime()) / ONE_DAY_IN_MILLIS);
    }

    public static Date addDaysToDate(Date date, int days) {
        long epochTime = date.getTime() + (days * ONE_DAY_IN_MILLIS);
        return new Date(epochTime);
    }

}
