package com.pretizy.activitytracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pretizy.activitytracker.model.Schedule;
import com.pretizy.activitytracker.model.ScheduleDao;

import java.util.Calendar;
import java.util.List;

/**
 * Created by gerald on 11/15/16.
 */
public class BootReceiver extends BroadcastReceiver {
    private ScheduleDao scheduleDao;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;


    @Override
    public void onReceive(Context context, Intent intent) {
        scheduleDao = new ScheduleDao(context);
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        List<Schedule> schedules = scheduleDao.findAllSchedules();
        for(Schedule s: schedules){
            Intent intent1 = new Intent(context, AlarmReceiver.class);
                // Set the alarm to start at approximately 2:00 p.m.
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.clear();
                calendar.setTime(s.getTime());


                intent1.putExtra("requestCode", s.getId().toString());
                pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), s.getId().intValue(), intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), pendingIntent);

            }
    }
}
