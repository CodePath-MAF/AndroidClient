
package org.missionassetfund.apps.android.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("result")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LCDetail {

    // TODO finish this class

    @JsonProperty("transactionsByDate")
    private Map<String, List<Transaction>> transactionsByDate;

    @JsonProperty("stackedBarChart")
    private Chart chart;

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public Map<String, List<Transaction>> getTransactionsByDate() {
        return transactionsByDate;
    }

    public void setTransactionsByDate(Map<String, List<Transaction>> transactionsByDate) {
        this.transactionsByDate = transactionsByDate;
    }

}
