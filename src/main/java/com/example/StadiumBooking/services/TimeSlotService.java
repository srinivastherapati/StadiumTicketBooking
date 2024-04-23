package com.example.StadiumBooking.services;

import com.example.StadiumBooking.DataModel.Stadium;
import com.example.StadiumBooking.DataModel.TimeSlot;
import com.example.StadiumBooking.repositeries.StadiumRepo;
import com.example.StadiumBooking.repositeries.TimeSlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSlotService {
    @Autowired
    private TimeSlotRepo timeSlotRepository;

    public ResponseEntity<?> addTimeSlot(TimeSlot timeSlot) {
        // Check for conflicts before adding the time slot
        if (hasConflict(timeSlot)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("slot is already booked chose other time");
        }
        timeSlotRepository.save(timeSlot);
        return ResponseEntity.status(HttpStatus.OK).body("time slot is booked ");

    }

    private boolean hasConflict(TimeSlot newTimeSlot) {
        List<TimeSlot> existingTimeSlots = timeSlotRepository.findByStadiumNameAndDate(newTimeSlot.getStadiumName(), newTimeSlot.getDate());

        for (TimeSlot existingTimeSlot : existingTimeSlots) {
            if (existingTimeSlot.getStartTime().isBefore(newTimeSlot.getEndTime()) &&
                    existingTimeSlot.getEndTime().isAfter(newTimeSlot.getStartTime())) {
                return true; // Conflict found
            }
        }
        return false;
    }
}
