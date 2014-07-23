
package org.missionassetfund.apps.android.receivers;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.GoalDetailsActivity;
import org.missionassetfund.apps.android.models.Goal;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class DismissNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int notificationId = intent.getIntExtra(PushNotificationReceiver.NOTIFICATION_ID_KEY, -1);
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (action.equals(PushNotificationReceiver.ADD_TRANSACTION_ACTION)) {
            manager.cancel(notificationId);
        } else if (action.equals(PushNotificationReceiver.PAYMENT_REMINDER_ACTION)) {
            Intent notificationIntent = new Intent(context, GoalDetailsActivity.class);
            notificationIntent.putExtra(Goal.GOAL_KEY, intent.getStringExtra(PushNotificationReceiver.GOAL_ID_KEY));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

            final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                    context)
                    .setContentTitle("Oh No!")
                    .setContentText("")
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Don't worry, Diana will help you get back on track"))
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher);
            
            manager.notify(notificationId, notificationBuilder.build());
        }
    }

}
