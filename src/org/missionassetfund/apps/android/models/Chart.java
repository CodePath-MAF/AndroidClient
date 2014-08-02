package org.missionassetfund.apps.android.models;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Chart {

    private BigDecimal maxValue;
    private Boolean hasData;
    private List<String> xLabels;
    private List<List<CategoryTotal>> data;

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public Boolean getHasData() {
        return hasData;
    }

    public void setHasData(Boolean hasData) {
        this.hasData = hasData;
    }

    public List<String> getxLabels() {
        return xLabels;
    }

    public void setxLabels(List<String> xLabels) {
        this.xLabels = xLabels;
    }

    public List<List<CategoryTotal>> getData() {
        return data;
    }

    public void setData(List<List<CategoryTotal>> data) {
        this.data = data;
    }
    
}
