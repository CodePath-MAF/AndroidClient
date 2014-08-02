
package org.missionassetfund.apps.android.models;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("lineChart")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashSpentChart {

    private List<String> xLabels;
    private List<BigDecimal> data;

    public List<String> getxLabels() {
        return xLabels;
    }

    public void setxLabels(List<String> xLabels) {
        this.xLabels = xLabels;
    }

    public List<BigDecimal> getData() {
        return data;
    }

    public void setData(List<BigDecimal> data) {
        this.data = data;
    }

}
