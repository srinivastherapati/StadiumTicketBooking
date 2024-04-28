package com.example.StadiumBooking.services;

import com.example.StadiumBooking.DataModel.Schedule;
import com.example.StadiumBooking.repositeries.ScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

            if (existingSchedule.getStartTime().isBefore(newSchedule.getEndTime()) &&
                    existingSchedule.getEndTime().isAfter(newSchedule.getStartTime())) {
                // Conflict found
                return true;
            }
        }
        return false; // No conflicts found
    }
}
