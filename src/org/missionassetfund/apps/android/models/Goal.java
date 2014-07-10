
package org.missionassetfund.apps.android.models;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Goal")
public class Goal extends ParseObject {
    public static final String USER_KEY = "user";
    public static final String NAME_KEY = "name";
    public static final String DESCRIPTION_KEY = "description";
    public static final String TYPE_KEY = "type";
    public static final String STATUS_KEY = "status";
    public static final String PAYMENT_INTERVAL_KEY = "paymentInterval";
    public static final String AMOUNT_KEY = "paymentAmount";
    public static final String NUM_PAYMENTS_KEY = "numPayments";
    public static final String GOAL_DATE_KEY = "goalDate";

    public Goal() {
    }

    public User getUser() {
        return (User) getParseUser(USER_KEY);
    }

    public void setUser(User user) {
        put(USER_KEY, user);
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
    }

    public String getDescription() {
        return getString(DESCRIPTION_KEY);
    }

    public void setDescription(String description) {
        put(DESCRIPTION_KEY, description);
    }

    // TODO: Discuss if hardcode 'type' via an enum or have a dummy Transaction
    // Type Model
    public String getType() {
        return getString(TYPE_KEY);
    }

    public void setType(String type) {
        put(TYPE_KEY, type);
    }

    public GoalStatus getStatus() {
        return GoalStatus.getTypeFromInt(getInt(STATUS_KEY));
    }

    public void setStatus(GoalStatus goalStatus) {
        put(STATUS_KEY, goalStatus.toInt());
    }

    public GoalPaymentInterval getPaymentInterval() {
        return GoalPaymentInterval.getTypeFromInt(getInt(PAYMENT_INTERVAL_KEY));
    }

    public void setPaymenyInterval(GoalPaymentInterval paymenyInterval) {
        put(PAYMENT_INTERVAL_KEY, paymenyInterval.toInt());
    }

    public Float getAmount() {
        return (Float) getNumber(AMOUNT_KEY);
    }

    public void setAmount(Float amount) {
        put(AMOUNT_KEY, amount);
    }

    public Integer getNumPayments() {
        return (Integer) getNumber(NUM_PAYMENTS_KEY);
    }

    public void setNumPayments(Integer numPayments) {
        put(NUM_PAYMENTS_KEY, numPayments);
    }

    public Date getGoalDate() {
        return getDate(GOAL_DATE_KEY);
    }

    public void setGoalDate(Date goalDate) {
        put(GOAL_DATE_KEY, goalDate);
    }
}
