
package org.missionassetfund.apps.android.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.missionassetfund.apps.android.utils.MAFDateUtils;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Goal")
public class Goal extends ParseObject {
    public static final String GOAL_KEY = "goal";

    public static final String USER_KEY = "user";
    public static final String NAME_KEY = "name";
    public static final String TYPE_KEY = "type";
    public static final String STATUS_KEY = "status";
    public static final String PAYMENT_INTERVAL_KEY = "paymentInterval";
    public static final String AMOUNT_KEY = "amount";
    public static final String PAYMENT_AMOUNT_KEY = "paymentAmount";
    public static final String CURRENT_TOTAL_KEY = "currentTotal";
    public static final String NUM_PAYMENTS_KEY = "numPayments";
    public static final String GOAL_DATE_KEY = "goalDate";
    public static final String GOAL_START_DATE_KEY = "createdAt";
    public static final String NUM_PAYMENTS_MADE_KEY = "numPaymentsMade";
    public static final String PARENT_GOAL_KEY = "parentGoal";
    public static final String USERS_KEY = "users";
    public static final String CASH_OUT_DATE_KEY = "cashOutDate";
    public static final String PAID_OUT_KEY = "paidOut";

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

    public GoalType getType() {
        return GoalType.getTypeFromInt(getInt(TYPE_KEY));
    }

    public void setType(GoalType goalType) {
        put(TYPE_KEY, goalType.toInt());
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

    public Double getAmount() {
        return getDouble(AMOUNT_KEY);
    }

    public void setAmount(Double amount) {
        put(AMOUNT_KEY, amount);
    }

    public Double getPaymentAmount() {
        return getDouble(PAYMENT_AMOUNT_KEY);
    }

    public void setPaymentAmount(Double amount) {
        put(PAYMENT_AMOUNT_KEY, amount);
    }

    public Double getCurrentTotal() {
        return getDouble(CURRENT_TOTAL_KEY);
    }

    public void setCurrentTotal(Double amount) {
        put(CURRENT_TOTAL_KEY, amount);
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

    public Integer getNumPaymentsMade() {
        return (Integer) getNumber(NUM_PAYMENTS_MADE_KEY);
    }

    public void setNumPaymentsMade(Integer numPaymentsMade) {
        put(NUM_PAYMENTS_MADE_KEY, numPaymentsMade);
    }

    public Goal getParentGoal() {
        return (Goal) getParseObject(PARENT_GOAL_KEY);
    }

    public void setParentGoal(Goal parentGoal) {
        put(PARENT_GOAL_KEY, parentGoal);
    }

    public List<User> getUsers() {
        return getList(USERS_KEY);
    }

    public void setUsers(ArrayList<User> users) {
        put(USERS_KEY, users);
    }

    public Date getCashOutDate() {
        return getDate(CASH_OUT_DATE_KEY);
    }

    public void setCashOutDate(Date cashOutDate) {
        put(CASH_OUT_DATE_KEY, cashOutDate);
    }

    public boolean isPaidOut() {
        return getBoolean(PAID_OUT_KEY);
    }

    public void setPaidOut(boolean paidOut) {
        put(PAID_OUT_KEY, paidOut);
    }

    public Date getDueDate() {
        int nextPaymentNum = getIdealNumPaymentsTillToday() + 1;
        return MAFDateUtils.addDaysToDate(getCreatedAt(), nextPaymentNum
                * getPaymentInterval().toInt());
    }

    public int getIdealNumPaymentsTillToday() {
        int daysSinceGoalCreation = MAFDateUtils.getDaysSince(getCreatedAt());
        int idealNumPayments = daysSinceGoalCreation / getPaymentInterval().toInt();
        return idealNumPayments;
    }

}
