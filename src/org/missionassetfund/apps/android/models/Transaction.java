
package org.missionassetfund.apps.android.models;

import java.math.BigDecimal;
import java.util.Date;

import org.missionassetfund.apps.android.utils.CurrencyUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Transaction")
@JsonIgnoreProperties(value = { "objectId", "goal" }, ignoreUnknown = true)
public class Transaction extends ParseObject {

    public static final String USER_KEY = "user";
    public static final String GOAL_KEY = "goal";
    public static final String TRANSACTION_DATE = "transactionDate";
    public static final String AMOUNT_KEY = "amount";
    public static final String NAME_KEY = "name";
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

    public Date getTransactionDate() {
        return getDate(TRANSACTION_DATE);
    }

    public void setTransactionDate(Date transactionDate) {
        put(TRANSACTION_DATE, transactionDate);
    }

    public Double getAmount() {
        return getDouble(AMOUNT_KEY);
    }

    public void setAmount(Double amount) {
        put(AMOUNT_KEY, amount);
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
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

    public String getAmountFormatted() {
        return this.getType() == TransactionType.CREDIT ? CurrencyUtils
                .getCurrencyValueFormattedAsNegative(BigDecimal
                        .valueOf(this.getAmount())) : CurrencyUtils
                .getCurrencyValueFormatted(BigDecimal
                        .valueOf(this.getAmount()));
    }

    public boolean isCredit() {
        return this.getType().equals(TransactionType.CREDIT);
    }

    public boolean isDebit() {
        return this.getType().equals(TransactionType.DEBIT);
    }
}
