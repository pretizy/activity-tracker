package com.pretizy.activitytracker.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.pretizy.activitytracker.AlarmReceiver;
import com.pretizy.activitytracker.BootReceiver;
import com.pretizy.activitytracker.R;
import com.pretizy.activitytracker.model.Schedule;
import com.pretizy.activitytracker.model.ScheduleDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateEventActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    @BindView(R.id.event_date)
    EditText date;
    @BindView(R.id.event_time)
    EditText time;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.done)
    Button done;
    @BindView(R.id.new_event_title)
    EditText title;
    @BindView(R.id.content)
    EditText content;
    private Date selectedDate;
    private String am_pm;
    private ScheduleDao scheduleDao;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        scheduleDao = new ScheduleDao(this);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        selectedDate = new Date();

        Calendar newCalendar = Calendar.getInstance();
        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);



        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                monthOfYear+=1;
                date.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                am_pm = "";

                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.MINUTE, minute);

                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "AM";
                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "PM";

                time.setText(hourOfDay + ":" + minute+" "+am_pm);
            }
        }, hour, minute, true);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    Schedule schedule = new Schedule();
                    schedule.setTitle(title.getText().toString());
                    schedule.setData(content.getText().toString());
                    schedule.setAm_pm(am_pm);
                    Date sdate = Schedule.retrieveDateFromString(date.getText().toString(), time.getText().toString());
                    schedule.setTime(sdate);
                    if(scheduleDao.createSchedule(schedule)){
                        // Set the alarm to start at approximately 2:00 p.m.
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.clear();
                        calendar.setTime(schedule.getTime());

                        ComponentName receiver = new ComponentName(getApplicationContext(), BootReceiver.class);
                        PackageManager pm = getApplicationContext().getPackageManager();

                        pm.setComponentEnabledSetting(receiver,
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP);
                        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                        intent.putExtra("requestCode", schedule.getId().toString());
                        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), schedule.getId().intValue(), intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(), pendingIntent);
                        finish();
                    }

                }
            }
        });

    }

    private boolean validate() {
        if(TextUtils.isEmpty(title.getText())){
            title.setError(getString(R.string.title_error));
            return false;
        }else if(TextUtils.isEmpty(content.getText())){
            content.setError(getString(R.string.content_error));
            return false;
        }else if(TextUtils.isEmpty(date.getText())){
            date.setError(getString(R.string.date_error));
            return false;
        }else if(TextUtils.isEmpty(time.getText())){
            time.setError(getString(R.string.time_error));
            return false;
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
