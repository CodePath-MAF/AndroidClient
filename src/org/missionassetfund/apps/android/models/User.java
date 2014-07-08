
package org.missionassetfund.apps.android.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser {
    public static final String NAME_KEY = "name";
    public static final String PHONE_NUMBER_KEY = "phoneNumber";
    
    public User() {
        super();
    }
    
    // TODO: determine where to store liquid asset data.

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
}
