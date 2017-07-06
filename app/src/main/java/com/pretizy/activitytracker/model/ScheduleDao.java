package com.pretizy.activitytracker.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by gerald on 10/5/16.
 */
public class ScheduleDao extends DbHelper {


    public ScheduleDao(Context context) {
        super(context);
    }

    @Override
    String getTableName() {
        return EventReaderContract.EventEntry.TABLE_NAME;
    }

    public boolean createSchedule(Schedule schedule){
// Create a new map of values, where column names are the keys
        Calendar c1 = Calendar.getInstance();
        c1.setTime(schedule.getTime());
        String date = c1.get(Calendar.DAY_OF_MONTH)+"/"+(c1.get(Calendar.MONTH)+1)+"/"+c1.get(Calendar.YEAR);
        String time = c1.get(Calendar.HOUR)+":"+c1.get(Calendar.MINUTE);


        ContentValues values = new ContentValues();
        values.put(EventReaderContract.EventEntry.COLUMN_NAME_TITLE, schedule.getTitle());
        values.put(EventReaderContract.EventEntry.COLUMN_NAME_CONTENT, schedule.getData());
        values.put(EventReaderContract.EventEntry.COLUMN_NAME_DATE, date);
        values.put(EventReaderContract.EventEntry.COLUMN_NAME_TIME, time);
        values.put(EventReaderContract.EventEntry.COLUMN_NAME_AM_PM, schedule.getAm_pm());

// Insert the new row, returning the primary key value of the new row
        schedule.setId(create(values));
        return schedule.getId()!=0? true:false;
    }

    public Schedule findSchedule(long id){

        return null;
    }

    public List<Schedule> findAllSchedules(){
        Cursor cursor = findAll();
        cursor.moveToFirst();
        List<Schedule> schedules = new ArrayList<>();
        while (cursor.moveToNext()){
            Schedule schedule= cursorToSchedule(cursor);
            schedules.add(schedule);
            schedule.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
        }


        return schedules;
    }

    public boolean delete(String id){
        return super.delete(id);
    }

    public Schedule findWithId(String id) {
        Cursor cursor =  super.findById(id);
        cursor.moveToFirst();
        return cursorToSchedule(cursor);
    }

    public Schedule cursorToSchedule(Cursor cursor){
        Schedule schedule =new Schedule();
        schedule.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(EventReaderContract.EventEntry.COLUMN_NAME_TITLE)));
        schedule.setData(cursor.getString(cursor.getColumnIndexOrThrow(EventReaderContract.EventEntry.COLUMN_NAME_CONTENT)));
        String date_string = cursor.getString(cursor.getColumnIndexOrThrow(EventReaderContract.EventEntry.COLUMN_NAME_DATE));
        String time_string = cursor.getString(cursor.getColumnIndexOrThrow(EventReaderContract.EventEntry.COLUMN_NAME_TIME));
        schedule.setAm_pm(cursor.getString(cursor.getColumnIndexOrThrow(EventReaderContract.EventEntry.COLUMN_NAME_AM_PM)));
        Date date = Schedule.retrieveDateFromString(date_string, time_string);
        schedule.setTime(date);
        schedule.setDate_string(date_string);
        schedule.setTime_string(time_string);
        return schedule;
    }
}
