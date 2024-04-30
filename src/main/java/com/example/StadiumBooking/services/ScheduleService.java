package com.example.StadiumBooking.services;

import com.example.StadiumBooking.DataModel.Schedule;
import com.example.StadiumBooking.repositeries.ScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepo scheduleRepo;

    public boolean isScheduleConflict(Schedule newSchedule) {
        List<Schedule> existingSchedules = scheduleRepo.findAll();

        for (Schedule existingSchedule : existingSchedules) {
            // Check for conflicts based on stadium and time range
            if (!existingSchedule.getStadiumName().equals(newSchedule.getStadiumName())) {
                // Schedules are in different stadiums, so no conflict
                continue;
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            // Parse the string to obtain a Date object
            Date existingStartDate=null;
            Date existingEndDate=null;
            Date newStartTime=null;
            Date newEndTime=null;
            try {
                existingStartDate = dateFormat.parse(existingSchedule.getStartTime());
                existingEndDate=dateFormat.parse(existingSchedule.getEndTime());
                newStartTime=dateFormat.parse(newSchedule.getStartTime());
                 newEndTime=dateFormat.parse(newSchedule.getEndTime());
                //   System.out.println("Date: " + date);
            } catch (java.text.ParseException e) {
                // Handle the parse exception
                e.printStackTrace();
            }
            if(existingStartDate!=null && existingEndDate!=null) {
                if (existingStartDate.before(newEndTime) &&
                        existingEndDate.after(newStartTime)) {
                    // Conflict found
                    return true;
                }
            }
        }
        return false; // No conflicts found
    }
    public  boolean checkScheduleConflictByStadium(String stadiumName, Date newStartTime, Date newEndDate){
        List<Schedule> scheduleListByStadium=scheduleRepo.findByStadiumName(stadiumName);
        if(scheduleListByStadium.isEmpty()){
            return false;
        }
        for(Schedule schedule:scheduleListByStadium){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            // Parse the string to obtain a Date object
            Date startDate=null;
            Date endDate=null;
            try {
                startDate = dateFormat.parse(schedule.getStartTime());
                endDate=dateFormat.parse(schedule.getEndTime());
                //   System.out.println("Date: " + date);
            } catch (java.text.ParseException e) {
                // Handle the parse exception
                e.printStackTrace();
            }
            if(startDate.before(newEndDate) && endDate.after(newStartTime)){
                return true;
            }
        }
        return false;
    }
}
