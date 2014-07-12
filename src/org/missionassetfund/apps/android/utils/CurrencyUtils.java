
package org.missionassetfund.apps.android.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class CurrencyUtils {
    
    public static String getCurrencyValueFormatted(BigDecimal value) {
        NumberFormat baseFormat = NumberFormat.getCurrencyInstance();
        return baseFormat.format(value);
    }
}
