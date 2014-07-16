
package org.missionassetfund.apps.android.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

public class CurrencyUtils {

    public static final BigDecimal ZERO = new BigDecimal("0.00");

    public static String getCurrencyValueFormatted(BigDecimal value) {
        NumberFormat baseFormat = NumberFormat.getCurrencyInstance();
        return baseFormat.format(value);
    }
    
    public static String getCurrencyValueFormatted(Double value) {
        return getCurrencyValueFormatted(newCurrency(value));
    }
    
    public static String getCurrencyValueFormattedAsNegative(BigDecimal value) {
        return String.format("- %s", getCurrencyValueFormatted(value));
    }
    
    public static BigDecimal newCurrency(Double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.DOWN);
    }

}
