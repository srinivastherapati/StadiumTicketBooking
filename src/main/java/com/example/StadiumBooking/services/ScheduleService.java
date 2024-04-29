package com.example.StadiumBooking.services;

import com.example.StadiumBooking.DataModel.Schedule;
import com.example.StadiumBooking.repositeries.ScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

            if (existingSchedule.getStartTime().before(newSchedule.getEndTime()) &&
                    existingSchedule.getEndTime().after(newSchedule.getStartTime())) {
                // Conflict found
                return true;
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
            if(schedule.getStartTime().before(newEndDate) && schedule.getEndTime().after(newStartTime)){
                return true;
            }
        }
        return false;
    }
}
