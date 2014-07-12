
package org.missionassetfund.apps.android.models;

import java.math.BigDecimal;
import java.util.Date;

import org.missionassetfund.apps.android.utils.CurrencyUtils;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Transaction")
public class Transaction extends ParseObject {
    public static final String USER_KEY = "user";
    public static final String GOAL_KEY = "goal";
    public static final String CREATED_AT_KEY = "createdAt";
    public static final String AMOUNT_KEY = "amount";
    public static final String DESCRIPTION_KEY = "description";
    public static final String TYPE_KEY = "type";
    public static final String CATEGORY_KEY = "category";

    public Transaction() {
        super();
    }

    public User getUser() {
        return (User) getParseUser(USER_KEY);
    }

    public void setUser(User user) {
        put(USER_KEY, user);
    }

    public Goal getGoal() {
        return (Goal) getParseObject(GOAL_KEY);
    }

    public void setGoal(Goal goal) {
        put(GOAL_KEY, goal);
    }

    public Date getCreatedAt() {
        return getDate(CREATED_AT_KEY);
    }

    public void setCreatedAt(Date createdAt) {
        put(CREATED_AT_KEY, createdAt);
    }

    public Float getAmount() {
        return (Float) getNumber(AMOUNT_KEY);
    }

    public void setAmount(Float amount) {
        put(AMOUNT_KEY, amount);
    }

    public String getDescription() {
        return getString(DESCRIPTION_KEY);
    }

    public void setDescription(String description) {
        put(DESCRIPTION_KEY, description);
    }

    // TODO: Discuss if hardcode 'type' via an enum or have a dummy Transaction Type Model
    public String getType() {
        return getString(TYPE_KEY);
    }

    public void setType(String type) {
        put(TYPE_KEY, type);
    }

    // TODO: Discuss if hardcode 'category' via an enum or have a dummy
    // Transaction Type Model
    public String getCategory() {
        return getString(CATEGORY_KEY);
    }

    public void setCategory(String category) {
        put(CATEGORY_KEY, category);
    }

    public String getAmountFormattedAsNegative() {
        return String.format("- %s",
                CurrencyUtils.getCurrencyValueFormatted(BigDecimal.valueOf(this.getAmount())));
    }
}
