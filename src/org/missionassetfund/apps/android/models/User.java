
package org.missionassetfund.apps.android.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String NAME_KEY = "name";
    public static final String PHONE_NUMBER_KEY = "phoneNumber";
    public static final String TOTAL_CASH_KEY = "totalCash";
    public static final String SETUP_KEY = "setup";

    public User() {
        super();
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
    }

    public String getPhoneNumber() {
        return getString(PHONE_NUMBER_KEY);
    }

    public void setPhoneNumber(String phoneNumber) {
        put(PHONE_NUMBER_KEY, phoneNumber);
    }
    
    public Double getTotalCash() {
        return getDouble(TOTAL_CASH_KEY);
    }
    
    public void setTotalCash(Double liquidAsset) {
        put(TOTAL_CASH_KEY, liquidAsset);
    }
    
    public boolean isSetup() {
        return getBoolean(SETUP_KEY);
    }
    
    public void setSetup(boolean setup) {
        put(SETUP_KEY, setup);
    }
}
