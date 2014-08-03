package org.missionassetfund.apps.android.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("result")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionsDashboard {
    
    private BigDecimal spentThisWeek;
    private BigDecimal spentToday;
    private BigDecimal totalCash;
    private List<String> dates;
    
    @JsonProperty("transactionsByDate")
    private TreeMap<String, List<Transaction>> transactionsByDate;
    
    @JsonProperty("stackedBarChart")
    private Chart chart;
    
    public BigDecimal getSpentThisWeek() {
        return spentThisWeek;
    }
    
    public void setSpentThisWeek(BigDecimal spentThisWeek) {
        this.spentThisWeek = spentThisWeek;
    }
    
    public BigDecimal getSpentToday() {
        return spentToday;
    }
    
    public void setSpentToday(BigDecimal spentToday) {
        this.spentToday = spentToday;
    }
    
    public BigDecimal getTotalCash() {
        return totalCash;
    }
    
    public void setTotalCash(BigDecimal totalCash) {
        this.totalCash = totalCash;
    }
    
    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public TreeMap<String, List<Transaction>> getTransactionsByDate() {
        return transactionsByDate;
    }

    public void setTransactionsByDate(TreeMap<String, List<Transaction>> transactionsByDate) {
        this.transactionsByDate = transactionsByDate;
    }

}
