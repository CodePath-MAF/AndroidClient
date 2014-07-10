
package org.missionassetfund.apps.android;

import org.missionassetfund.apps.android.models.Category;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.User;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class MAFApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register the parse models
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Transaction.class);
        ParseObject.registerSubclass(Goal.class);
        ParseObject.registerSubclass(Category.class);

        // Initializing Parse
        Parse.initialize(this, getString(R.string.parseApplicationId),
                getString(R.string.parseClientId));
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
    }
}
