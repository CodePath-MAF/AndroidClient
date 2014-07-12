
package org.missionassetfund.apps.android.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.text.format.DateUtils;

public class FormatterUtils {

    public static String formatAmount(Double amount) {
        return String.format(Locale.US, "%.2f", amount);
    }

    public static CharSequence formatMonthDate(Date date) {
        DateFormat monthDayFormat = new SimpleDateFormat("MMM dd", Locale.US);
        return monthDayFormat.format(date);
    }

    public static String getRelativeTimeHuman(Date date) {
        return DateUtils.getRelativeTimeSpanString(date.getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
    }

}
