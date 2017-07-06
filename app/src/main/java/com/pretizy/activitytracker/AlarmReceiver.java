package com.pretizy.activitytracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.pretizy.activitytracker.model.Schedule;
import com.pretizy.activitytracker.model.ScheduleDao;
import com.pretizy.activitytracker.ui.EventActivity;

/**
 * Created by gerald on 11/14/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private ScheduleDao scheduleDao;

    @Override
    public void onReceive(Context context, Intent intent) {
        scheduleDao =new ScheduleDao(context);
        String id = intent.getStringExtra("requestCode");
        Schedule schedule = scheduleDao.findWithId(id);
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        if(ringtone!=null)
        ringtone.play();
        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.newlogo)
                        .setContentTitle("EPS Tracker Alarm")
                        .setContentText(schedule.getTitle());
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, EventActivity.class);
        resultIntent.putExtra("title", schedule.getTitle());
        resultIntent.putExtra("id", schedule.getId());
        resultIntent.putExtra("description", schedule.getData());
        resultIntent.putExtra("time", schedule.getTime()+" ");


// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

    }
}
