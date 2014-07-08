
package org.missionassetfund.apps.android;

import android.app.Application;

import com.parse.Parse;

public class MAFApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initializing Parse
        Parse.initialize(this, "qK7qJuFt6weBIrBx9eTzK1UBWJvkqb3jH6l8aw22",
                "SPC4XFKVlnX4ChVu7jS0IwTjfKDAY9uxXh1Y8jsy");
        
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
    }

}
