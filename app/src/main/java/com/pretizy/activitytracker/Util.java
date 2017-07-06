package com.pretizy.activitytracker;

import com.pretizy.activitytracker.model.Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by gerald on 11/16/16.
 */
public class Util {
    public static Schedule codeTOSchedule(String coded){
        Schedule schedule = new Schedule();
        String [] protocols = coded.split("-");
        if(protocols.length == 5) {
            schedule.setData(protocols[0]);
            schedule.setTitle(protocols[1]);
            schedule.setTime(Schedule.retrieveDateFromString(protocols[2], protocols[3]));
            schedule.setAm_pm(protocols[4]);
        }
        return schedule;
    }

    public static String ScheduleToCode(Schedule schedule){
        String code = schedule.getData()+"-"+schedule.getTitle()+"-";
        code += schedule.getDate_string()+"-"+schedule.getTime_string()+"-";
        code += schedule.getAm_pm();
        return code;
    }

    public static List<Schedule> sortSchedule(List<Schedule> unsorted){
        Collections.sort(unsorted, new Comparator<Schedule>() {
            @Override
            public int compare(Schedule lhs, Schedule rhs) {
                if (lhs.getTime().after(rhs.getTime())){
                    return 1;
                }
                if(lhs.getTime().before(rhs.getTime())){
                    return -1;
                }
                return 0;
            }
        });

        return unsorted;
    }

    public static List<Schedule> getImediate15(List<Schedule> unsorted){
        sortSchedule(unsorted);
        List<Schedule> top15 = new ArrayList<>();
        for (Schedule schedule : unsorted) {
            if(schedule.getTime().after(new Date())){
                top15.add(schedule);
            }
        }
        return top15;
    }
}
