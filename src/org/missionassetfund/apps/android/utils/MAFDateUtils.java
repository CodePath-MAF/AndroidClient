
package org.missionassetfund.apps.android.utils;

import java.util.Calendar;
import java.util.Date;

import android.text.format.DateUtils;

public class MAFDateUtils {
    private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    public static int getDaysSince(Date date) {
        if (date == null) {
            return 0;
        }
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

    public static boolean isSameWeek(Date date) {
        Calendar beginningOfTheWeek = Calendar.getInstance();
        beginningOfTheWeek.set(Calendar.DAY_OF_WEEK, beginningOfTheWeek.getFirstDayOfWeek());
        beginningOfTheWeek.set(Calendar.HOUR, 0);
        beginningOfTheWeek.set(Calendar.MINUTE, 0);

        Calendar endOfTheWeek = Calendar.getInstance();
        endOfTheWeek.set(Calendar.DAY_OF_WEEK, endOfTheWeek.getFirstDayOfWeek());
        endOfTheWeek.add(Calendar.DAY_OF_WEEK, 7);
        endOfTheWeek.set(Calendar.HOUR, 23);
        endOfTheWeek.set(Calendar.MINUTE, 59);

        return date.after(beginningOfTheWeek.getTime()) && date.before(endOfTheWeek.getTime());
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static String getRelativeDate(Date date) {
        return DateUtils.getRelativeTimeSpanString(date.getTime(), new Date().getTime(),
                DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
    }
}
