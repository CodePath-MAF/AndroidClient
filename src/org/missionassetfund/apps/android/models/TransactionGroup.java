
package org.missionassetfund.apps.android.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.missionassetfund.apps.android.utils.CurrencyUtils;
import org.missionassetfund.apps.android.utils.MAFDateUtils;

import android.text.format.DateUtils;

public class TransactionGroup {

    private Date transactionDate;
    private List<Transaction> transactions;
    private BigDecimal spentAmount;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEE (MM/dd)", Locale.US);
    private Map<Category, BigDecimal> transactionGroupPercentageByCategory; 

    public TransactionGroup(Date transactionDate, List<Transaction> transactions) {
        super();
        this.transactionDate = transactionDate;
        this.transactions = transactions;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public CharSequence getRelativeDate() {
        return DateUtils.getRelativeTimeSpanString(this.transactionDate.getTime(),
                new Date().getTime(), DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
    }
    
    public String getTransactionDateFormatted() {
        return sdf.format(this.transactionDate);
    }
    
    public BigDecimal getSpentAmount() {
        if (spentAmount == null) {
            spentAmount = CurrencyUtils.ZERO;

            for (Transaction transaction : this.getTransactions()) {
                if (transaction.isCredit()) {
                    spentAmount = spentAmount.add(CurrencyUtils.newCurrency(transaction.getAmount()));
                }
            }
        }
        
        return spentAmount;
    }
    
    public Map<Category, BigDecimal> getTransactionGroupPercentageByCategory() {
        if (transactionGroupPercentageByCategory == null) {
            transactionGroupPercentageByCategory = new HashMap<Category, BigDecimal>();
            
            if (transactions == null || transactions.isEmpty()) {
                return transactionGroupPercentageByCategory;
            }
            
            BigDecimal currentPercentage = CurrencyUtils.ZERO;
            
            for (Transaction transaction : transactions) {
                if (transaction.isDebit()) {
                    break;
                }
                
                BigDecimal percentage = transactionGroupPercentageByCategory.get(transaction.getCategory());
                
                if (percentage == null) {
                    percentage = CurrencyUtils.ZERO;
                }
                
                currentPercentage = CurrencyUtils.newCurrency(transaction.getAmount()).divide(this.getSpentAmount(), 2, RoundingMode.DOWN);
                percentage = percentage.add(currentPercentage);
                
                transactionGroupPercentageByCategory.put(transaction.getCategory(), percentage);
            }
        }
        
        return transactionGroupPercentageByCategory;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((transactionDate == null) ? 0 : transactionDate.hashCode());
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
        if (transactionDate == null) {
            if (other.transactionDate != null)
                return false;
        } else if (!MAFDateUtils.isSameDay(transactionDate, other.getTransactionDate()))
            return false;
        return true;
    }

}
