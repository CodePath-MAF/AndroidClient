
package org.missionassetfund.apps.android;

import org.missionassetfund.apps.android.activities.MainActivity;
import org.missionassetfund.apps.android.models.Category;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.User;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.PushService;

public class MAFApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Register default font
        CalligraphyConfig.initDefault("fonts/OpenSans-Regular.ttf", R.attr.fontPath);

        // Register the parse models
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Transaction.class);
        ParseObject.registerSubclass(Goal.class);
        ParseObject.registerSubclass(Category.class);

        // Initializing Parse
        Parse.initialize(this, getString(R.string.parseApplicationId),
                getString(R.string.parseClientId));
        PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
    }
}
