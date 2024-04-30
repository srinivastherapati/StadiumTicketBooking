package com.example.StadiumBooking.Controllers;

import com.example.StadiumBooking.DataModel.Schedule;
import com.example.StadiumBooking.DataModel.Stadium;
import com.example.StadiumBooking.repositeries.ScheduleRepo;
import com.example.StadiumBooking.repositeries.StadiumRepo;
import com.example.StadiumBooking.services.ScheduleService;
import com.example.StadiumBooking.services.StadiumManagerService;
import com.example.StadiumBooking.services.StadiumService;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/schedule")
@CrossOrigin
public class ScheduleController {
    @Autowired
    private ScheduleRepo scheduleRepo;
    @Autowired
    private StadiumService stadiumService;
    @Autowired
    private StadiumManagerService stadiumManagerService;
    @Autowired
    private StadiumRepo stadiumRepo;
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/add")
    public ResponseEntity<?> addSchedule(@RequestBody Schedule schedule){
        Stadium isStadiumExists=stadiumService.IsStadiumExists(schedule.getStadiumName());
        if(isStadiumExists==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("stadium with name " +schedule.getStadiumName() +" does not exists");
        }
        boolean isAuthorized=stadiumManagerService.isAuthorized(schedule.getStadiumManagerEmail());
        if(!isAuthorized){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not authorized to schedule a game ,not approved by admin");
        }
        if(schedule.getAwayTeam().equals(schedule.getHomeTeam())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("homeTeam and away team can't be same");
        }
        if(schedule.getStartTime().equals(schedule.getEndTime())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("start and end time should be same");
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        // Parse the string to obtain a Date object
        Date startDate=null;
        Date endDate=null;
        try {
            startDate = dateFormat.parse(schedule.getStartTime());
            System.out.println("after format===="+startDate);
            endDate=dateFormat.parse(schedule.getEndTime());
            //   System.out.println("Date: " + date);
        } catch (java.text.ParseException e) {
            // Handle the parse exception
            e.printStackTrace();
        }
        Date currentTime = new Date();
        if (startDate.before(currentTime) || endDate.before(currentTime)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start time or end time cannot be in the past");
        }

        if (startDate.after(endDate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start time must be before end time");
        }
        if(schedule.getStartTime().equals(schedule.getEndTime())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("start and end time shouldn't be same");
        }

        if (scheduleService.isScheduleConflict(schedule)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Scheduling conflict with existing game");
        }

        System.out.println(">>>>>>>>>>"+schedule.getStartTime());
        schedule.setAvailableSeats(isStadiumExists.getCapacity());
        scheduleRepo.save(schedule);


        return ResponseEntity.status(HttpStatus.CREATED).body("scheduled successfully");

    }
    @PatchMapping("/edit-schedule/{id}")
    private ResponseEntity<?> editSchedule(@PathVariable String id,@RequestBody Schedule schedule){
        Optional<Schedule> existingSchedule=scheduleRepo.findById(id);
        if(existingSchedule.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("schedule does not exists");
        }
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
        Date currentTime = new Date();
        if (startDate.before(currentTime) || endDate.before(currentTime)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start time or end time cannot be in the past");
        }

        if (startDate.after(endDate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start time must be before end time");
        }
        if(schedule.getStartTime().equals(schedule.getEndTime())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("start and end time shouldn't be same");
        }
//        List<Schedule> conflictingSchedules = scheduleRepo.findConflictingSchedules(existingSchedule.get().getStadiumName(), schedule.getStartTime(),
//                schedule.getEndTime(), existingSchedule.get().getStartTime(), existingSchedule.get().getEndTime(),existingSchedule.get().getId());

       boolean conflictingSchedules=scheduleService.checkScheduleConflictByStadium(existingSchedule.get().getStadiumName(),startDate,endDate);
        if (conflictingSchedules) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Schedule conflicts with existing schedule(s)");
        }
        existingSchedule.get().setStartTime(schedule.getStartTime());
        existingSchedule.get().setEndTime(schedule.getEndTime());
        scheduleRepo.save(existingSchedule.get());
        return ResponseEntity.ok(existingSchedule);

    }
    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> deleteSchedule(@PathVariable String id){
        Optional<Schedule> existingSchedule=scheduleRepo.findById(id);
        if(existingSchedule.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("schedule does not exists");
        }
        scheduleRepo.delete(existingSchedule.get());
        return ResponseEntity.status(HttpStatus.OK).body("deleted successfully");
    }

    @GetMapping("/stadium/{stadiumName}")
    public ResponseEntity<List<Schedule>> getScheduledGamesByStadium(@PathVariable String stadiumName) {
        List<Schedule> scheduledGames = scheduleRepo.findByStadiumName(stadiumName);
        if (scheduledGames.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(scheduledGames);
    }

}
