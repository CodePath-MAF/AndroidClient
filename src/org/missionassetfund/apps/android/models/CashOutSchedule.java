package org.missionassetfund.apps.android.models;

public class CashOutSchedule {
    
    private String userId;
    private boolean paidOut;
    private Integer profileImageId;
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public boolean isPaidOut() {
        return paidOut;
    }
    
    public void setPaidOut(boolean paidOut) {
        this.paidOut = paidOut;
    }

    public Integer getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(Integer profileImageId) {
        this.profileImageId = profileImageId;
    }
    
}
