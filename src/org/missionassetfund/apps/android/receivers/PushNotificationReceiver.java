
package org.missionassetfund.apps.android.receivers;

import org.json.JSONException;
import org.json.JSONObject;
import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.AddTransactionActivity;
import org.missionassetfund.apps.android.activities.LiquidAssetsActivity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class PushNotificationReceiver extends BroadcastReceiver {

    private static final String TRANSACTION_NAME_KEY = "transaction_name";
    private static final String CATEGORY_ID_KEY = "category_id";
    private static final String MESSAGE_KEY = "message";
    private static final String TITLE_KEY = "title";
    private static final String TAG = "PushNotificationReceiver";
    private static final String ADD_TRANSACTION_ACTION = "org.missionassetfund.apps.android.ADD_TRANSACTION";
    private static final String PAYMENT_REMINDER_ACTION = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action == null) {
            return;
        }

        try {
            JSONObject data = (JSONObject) new JSONObject(intent.getExtras().getString("com.parse.Data")).get("data");

            String title = data.getString(TITLE_KEY);
            String message = data.getString(MESSAGE_KEY);

            if (action.equals(ADD_TRANSACTION_ACTION)) {
                Intent notificationIntent = new Intent(context, AddTransactionActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                notificationIntent.putExtra(CATEGORY_ID_KEY, data.getString(CATEGORY_ID_KEY));
                notificationIntent.putExtra(TRANSACTION_NAME_KEY, data.getString(TRANSACTION_NAME_KEY));
                
                postNotification(context, title, message, notificationIntent, LiquidAssetsActivity.class);
            } else if (action.equals(PAYMENT_REMINDER_ACTION)) {
                // TODO: waiting for goal to be an activity
            }

        } catch (JSONException e) {
            Log.d(TAG, "error parsing JSON", e);
        }
    }

    private void postNotification(Context context, String title, String message,
            Intent notificationIntent, Class<? extends Activity> parentActivity) {
        final NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(parentActivity);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT | Notification.DEFAULT_LIGHTS
                                | Notification.FLAG_AUTO_CANCEL
                        );

        final Notification notification = notificationBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        mNotificationManager.notify(0, notification);
    }

}
