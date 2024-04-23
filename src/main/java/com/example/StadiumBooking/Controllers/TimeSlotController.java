package com.example.StadiumBooking.Controllers;

import com.example.StadiumBooking.DataModel.TimeSlot;
import com.example.StadiumBooking.services.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timeSlot")
public class TimeSlotController {
    @Autowired
    private TimeSlotService timeSlotService;

    @PostMapping("/add")
    public ResponseEntity<?> addTimeSlot(@RequestBody TimeSlot timeSlot) {
        try {
           return timeSlotService.addTimeSlot(timeSlot);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the time slot");
        }
    }
}
