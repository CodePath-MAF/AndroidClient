
package com.missionassetfund.apps.android.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatterUtils {

    public static String formatAmount(Double amount) {
        return String.format(Locale.US, "%.2f", amount);
    }

    public static CharSequence formatMonthDate(Date date) {
        if (date == null) {
            return "NULL";
        }
        DateFormat monthDayFormat = new SimpleDateFormat("MMM dd", Locale.US);
        return monthDayFormat.format(date);
    }

}
