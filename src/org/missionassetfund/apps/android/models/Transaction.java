
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
    // Parse stores created at for ParseObject. Why do we need to do it
    // manually?
    // public static final String CREATED_AT_KEY = "createdAt";
    public static final String AMOUNT_KEY = "amount";
    public static final String DESCRIPTION_KEY = "description";
    public static final String TYPE_KEY = "type";
    public static final String CATEGORY_KEY = "category";

    public enum TransactionType {
        UNDEFINED(0),
        DEBIT(1),
        CREDIT(2);

        private int typeValue;

        private TransactionType(int typeValue) {
            this.typeValue = typeValue;
        }

        public int toInt() {
            return typeValue;
        }

        public static TransactionType getTypeFromInt(int typeValue) {
            switch (typeValue) {
                case 1:
                    return DEBIT;
                case 2:
                    return CREDIT;
                default:
                    return UNDEFINED;
            }
        }
    }

    public Transaction() {
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

    // Parse stores created at for ParseObject. Why do we need to do it
    // manually?
    // public Date getCreatedAt() {
    // return getDate(CREATED_AT_KEY);
    // }
    //
    // public void setCreatedAt(Date createdAt) {
    // put(CREATED_AT_KEY, createdAt);
    // }

    public Double getAmount() {
        return getDouble(AMOUNT_KEY);
    }

    public void setAmount(Double amount) {
        put(AMOUNT_KEY, amount);
    }

    public String getDescription() {
        return getString(DESCRIPTION_KEY);
    }

    public void setDescription(String description) {
        put(DESCRIPTION_KEY, description);
    }

    public TransactionType getType() {
        return TransactionType.getTypeFromInt(getInt(TYPE_KEY));
    }

    public void setType(TransactionType type) {
        put(TYPE_KEY, type.toInt());
    }

    public Category getCategory() {
        return (Category) getParseObject(CATEGORY_KEY);
    }

    public void setCategory(Category category) {
        put(CATEGORY_KEY, category);
    }

    public String getAmountFormattedAsNegative() {
        return String.format("- %s",
                CurrencyUtils.getCurrencyValueFormatted(BigDecimal.valueOf(this.getAmount())));
    }
}
