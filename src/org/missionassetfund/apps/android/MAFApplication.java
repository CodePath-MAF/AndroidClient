
package org.missionassetfund.apps.android;

import android.app.Application;

import com.parse.Parse;

public class MAFApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initializing Parse
        Parse.initialize(this, "YeCq7QZpwMg4vEqaZlJs2ZVZvQ7gJrTi1zN9aa4t",
                "DgHFqxILVyV2qIGA0pLNrbMlNMQBqDIpI5PZjV9x");
    }

}
