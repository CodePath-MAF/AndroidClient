
package org.missionassetfund.apps.android.receivers;

import org.json.JSONException;
import org.json.JSONObject;
import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.AddGoalPaymentActivity;
import org.missionassetfund.apps.android.activities.AddTransactionActivity;
import org.missionassetfund.apps.android.activities.GoalDetailsActivity;
import org.missionassetfund.apps.android.activities.LiquidAssetsActivity;
import org.missionassetfund.apps.android.models.Goal;

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

    public static final String NOTIFICATION_ID_KEY = "notification_id";
    private static final String TRANSACTION_NAME_KEY = "transaction_name";
    private static final String CATEGORY_ID_KEY = "category_id";
    private static final String MESSAGE_KEY = "message";
    private static final String TITLE_KEY = "title";
    private static final String TAG = "PushNotificationReceiver";
    private static final String ADD_TRANSACTION_ACTION = "org.missionassetfund.apps.android.ADD_TRANSACTION";
    private static final String PAYMENT_REMINDER_ACTION = "org.missionassetfund.apps.android.MAKE_PAYMENT";
    private static final int ADD_TRANSACTION_NOTIFICATION_ID = 1;
    private static final int MAKE_PAYMENT_NOTIFICATION_ID = 2;
    private static final String GOAL_ID_KEY = "goal_id";
    
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
                postAddTransactionNotification(context, title, message, data);
            } else if (action.equals(PAYMENT_REMINDER_ACTION)) {
                postMakePaymentNotification(context, title, message, data);
            }

        } catch (JSONException e) {
            Log.d(TAG, "error parsing JSON", e);
        }
    }

    private void postAddTransactionNotification(Context context, String title, String message,
            JSONObject data) throws JSONException {
        Intent notificationIntent = new Intent(context, AddTransactionActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra(CATEGORY_ID_KEY, data.getString(CATEGORY_ID_KEY));
        notificationIntent.putExtra(TRANSACTION_NAME_KEY, data.getString(TRANSACTION_NAME_KEY));
        notificationIntent.putExtra(NOTIFICATION_ID_KEY, ADD_TRANSACTION_NOTIFICATION_ID);

        final NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LiquidAssetsActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT | Notification.DEFAULT_LIGHTS
                                | Notification.FLAG_AUTO_CANCEL
                        );
        
        Intent dismissIntent = new Intent(context, DismissNotificationReceiver.class);
        dismissIntent.putExtra(NOTIFICATION_ID_KEY, ADD_TRANSACTION_NOTIFICATION_ID);
        
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final Notification notification = notificationBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .addAction(R.drawable.btn_check, context.getResources().getText(R.string.yes), pendingIntent)
                .addAction(R.drawable.btn_close_white_up, context.getResources().getText(R.string.no), dismissPendingIntent)
                .build();

        mNotificationManager.notify(ADD_TRANSACTION_NOTIFICATION_ID, notification);
    }

    private void postMakePaymentNotification(Context context, String title, String message,
            JSONObject data) throws JSONException {
        Intent notificationIntent = new Intent(context, AddGoalPaymentActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra(Goal.GOAL_KEY, data.getString(GOAL_ID_KEY));
        notificationIntent.putExtra(NOTIFICATION_ID_KEY, MAKE_PAYMENT_NOTIFICATION_ID);

        final NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(GoalDetailsActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT | Notification.DEFAULT_LIGHTS
                                | Notification.FLAG_AUTO_CANCEL
                        );
        
        Intent dismissIntent = new Intent(context, DismissNotificationReceiver.class);
        dismissIntent.putExtra(NOTIFICATION_ID_KEY, MAKE_PAYMENT_NOTIFICATION_ID);
        
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final Notification notification = notificationBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .addAction(R.drawable.btn_check, context.getResources().getText(R.string.yes), pendingIntent)
                .addAction(R.drawable.btn_close_white_up, context.getResources().getText(R.string.no), dismissPendingIntent)
                .build();

        mNotificationManager.notify(MAKE_PAYMENT_NOTIFICATION_ID, notification);
    }
}
