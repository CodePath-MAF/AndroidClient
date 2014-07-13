
package org.missionassetfund.apps.android.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String NAME_KEY = "name";
    public static final String PHONE_NUMBER_KEY = "phoneNumber";
    public static final String LIQUID_ASSETS_KEY = "liquidAssets";

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
    
    // For now, lets store liquid assets within the user Obj
    public Double getLiquidAssets() {
        return getDouble(LIQUID_ASSETS_KEY);
    }
    
    public void setLiquidAssets(Double liquidAsset) {
        put(LIQUID_ASSETS_KEY, liquidAsset);
    }
}
