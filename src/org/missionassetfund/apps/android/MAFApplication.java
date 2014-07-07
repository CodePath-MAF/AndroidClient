
package org.missionassetfund.apps.android;

import android.app.Application;

import com.parse.Parse;

public class MAFApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initializing Parse
        Parse.initialize(this, getString(R.string.parseApplicationId),
                getString(R.string.parseClientId));
    }

}
