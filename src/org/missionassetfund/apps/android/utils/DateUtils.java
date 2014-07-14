
package org.missionassetfund.apps.android.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static boolean isToday(Date date) {
        return android.text.format.DateUtils.isToday(date.getTime());
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
}
