
package org.missionassetfund.apps.android.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.missionassetfund.apps.android.R;

import android.content.Context;
import android.text.format.DateUtils;

public class FormatterUtils {

    public static String formatAmount(Double amount) {
        return String.format(Locale.US, "%.2f", amount);
    }

    public static CharSequence formatMonthDate(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat monthDayFormat = new SimpleDateFormat("MMM dd", Locale.US);
        return monthDayFormat.format(date).toUpperCase(Locale.US);
    }

    public static String getRelativeTimeHuman(Date date) {
        if (date == null) {
            return "";
        }
        return DateUtils.getRelativeTimeSpanString(date.getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString()
                .toUpperCase(Locale.US);
    }

    public static CharSequence getGoalDueDateCustomFormat(Context context, Date goalDate) {
        return context.getString(R.string.goal_due_date_custom_format,
                getRelativeTimeHuman(goalDate), formatMonthDate(goalDate));
    }
    
    public static String formatPercentage(BigDecimal value) {
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);
        
        return percentFormat.format(value);
    }
    
}
