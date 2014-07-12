package org.missionassetfund.apps.android.models;

import java.util.Date;
import java.util.List;

import android.text.format.DateUtils;

public class TransactionGroup {
    
    private Date createdAt;
    private List<Transaction> transactions;
    
    public TransactionGroup(Date createdAt, List<Transaction> transactions) {
        super();
        this.createdAt = createdAt;
        this.transactions = transactions;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
    
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    public CharSequence getRelativeDate() {
        return DateUtils.getRelativeTimeSpanString(this.createdAt.getTime(), new Date().getTime(), DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TransactionGroup other = (TransactionGroup) obj;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        return true;
    }

}
