
package org.missionassetfund.apps.android.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LCDetail {

    private List<CashOutSchedule> cashOutSchedule;
    
    @JsonProperty("isLendingCircle")
    private boolean isLendingCircle;

    public boolean isLendingCircle() {
        return isLendingCircle;
    }

    public void setLendingCircle(boolean isLendingCircle) {
        this.isLendingCircle = isLendingCircle;
    }

    public List<CashOutSchedule> getCashOutSchedule() {
        return cashOutSchedule;
    }

    public void setCashOutSchedule(List<CashOutSchedule> cashOutSchedule) {
        this.cashOutSchedule = cashOutSchedule;
    }
    
}
