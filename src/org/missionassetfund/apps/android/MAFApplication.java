
package org.missionassetfund.apps.android;

import org.missionassetfund.apps.android.activities.MainActivity;
import org.missionassetfund.apps.android.models.Category;
import org.missionassetfund.apps.android.models.Comment;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.Post;
import org.missionassetfund.apps.android.models.Transaction;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.models.dao.CategoryDao;

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
        // TODO: support portrait and landscape.

        // Register default font
        CalligraphyConfig.initDefault("fonts/OpenSans-Regular.ttf", R.attr.fontPath);

        // Register the parse models
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Transaction.class);
        ParseObject.registerSubclass(Goal.class);
        ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);

        // Enable local datastore
        Parse.enableLocalDatastore(this);

        // Initializing Parse
        Parse.initialize(this, getString(R.string.parseApplicationId),
                getString(R.string.parseClientId));
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        
        // Pin Categories locally
        new CategoryDao().pin();
    }
}
