package com.pretizy.activitytracker.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gerald on 10/3/16.
 */
public class Schedule {
    private Long id;
    private String title;
    private Date time;
    private String am_pm;
    private String data;
    private String date_string;
    private String time_string;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAm_pm() {
        return am_pm;
    }

    public void setAm_pm(String am_pm) {
        this.am_pm = am_pm;
    }

    public static Date retrieveDateFromString(String date, String time){
        try {
            Date date1 = dateFormat.parse(date);
            Date date2 = timeFormat.parse(time);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date1);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(date2);


            c1.set(Calendar.HOUR, c2.get(Calendar.HOUR));
            c1.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));
            return c1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }

    public String getDate_string() {
        return date_string;
    }

    public void setDate_string(String date_string) {
        this.date_string = date_string;
    }

    public String getTime_string() {
        return time_string;
    }

    public void setTime_string(String time_string) {
        this.time_string = time_string;
    }
}
