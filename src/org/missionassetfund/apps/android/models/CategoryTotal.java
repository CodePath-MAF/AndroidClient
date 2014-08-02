package org.missionassetfund.apps.android.models;

import java.math.BigDecimal;

public class CategoryTotal {

    private String categoryName;
    private BigDecimal categoryTotal;
    private String categoryColor;
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public BigDecimal getCategoryTotal() {
        return categoryTotal;
    }
    
    public void setCategoryTotal(BigDecimal categoryTotal) {
        this.categoryTotal = categoryTotal;
    }
    
    public String getCategoryColor() {
        return categoryColor;
    }
    
    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }
    
}
