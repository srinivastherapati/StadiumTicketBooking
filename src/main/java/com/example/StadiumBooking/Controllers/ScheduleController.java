package com.example.StadiumBooking.Controllers;

import com.example.StadiumBooking.DataModel.Schedule;
import com.example.StadiumBooking.DataModel.Stadium;
import com.example.StadiumBooking.repositeries.ScheduleRepo;
import com.example.StadiumBooking.repositeries.StadiumRepo;
import com.example.StadiumBooking.services.StadiumManagerService;
import com.example.StadiumBooking.services.StadiumService;
import com.example.StadiumBooking.services.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleRepo scheduleRepo;
    @Autowired
    private StadiumService stadiumService;
    @Autowired
    private StadiumManagerService stadiumManagerService;
    @Autowired
    private TimeSlotService timeSlotService;
    @Autowired
    private StadiumRepo stadiumRepo;
    @PostMapping("/add")
    public ResponseEntity<?> addSchedule(@RequestBody Schedule schedule){
        boolean isStadiumExists=stadiumService.IsStadiumExists(schedule.getStadiumName());
        if(!isStadiumExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("stadium with name " +schedule.getStadiumName() +" does not exists");
        }
        boolean isAuthorized=stadiumManagerService.isAuthorized(schedule.getStadiumManagerEmail());
        if(!isAuthorized){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not authorized to schedule a game ,not approved by admin");
        }
        if(schedule.getAwayTeam().equals(schedule.getHomeTeam())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("homeTeam and away team can't be same");
        }
        scheduleRepo.save(schedule);


        return ResponseEntity.status(HttpStatus.CREATED).body("scheduled successfully");

    }
    @PatchMapping("/edit-schedule/{id}")
    private ResponseEntity<?> editSchedule(@PathVariable String id,@RequestBody Schedule schedule){
        Optional<Schedule> existingSchedule=scheduleRepo.findById(id);
        if(existingSchedule.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("schedule does not exists");
        }
        existingSchedule.get().setDuration(schedule.getDuration());
        existingSchedule.get().setGameTitle(schedule.getGameTitle());
        existingSchedule.get().setNo_of_players_in_team(schedule.getNo_of_players_in_team());
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
    @GetMapping("/stadium/{stadiumId}/schedule")
    public ResponseEntity<?> getSchedule(@PathVariable String stadiumId){
        Optional<Stadium> existingStadium= stadiumRepo.findById(stadiumId);
        if(existingStadium.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("stadium does not exists");
        }
        Schedule schedule= scheduleRepo.findByStadiumName(existingStadium.get().getName());
        if(schedule==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("game not scheduled for this stadium");
        }
        return ResponseEntity.ok(schedule);
    }
}
