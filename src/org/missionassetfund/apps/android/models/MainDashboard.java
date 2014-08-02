
package org.missionassetfund.apps.android.models;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("result")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainDashboard {

    @JsonProperty("lineChart")
    private CashSpentChart cashSpentChart;
    private List<Goal> goals;
    private BigDecimal totalCash;

    public CashSpentChart getCashSpentChart() {
        return cashSpentChart;
    }

    public void setCashSpentChart(CashSpentChart cashSpentChart) {
        this.cashSpentChart = cashSpentChart;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public BigDecimal getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(BigDecimal totalCash) {
        this.totalCash = totalCash;
    }

}
