package com.example.StadiumBooking.Controllers;

import com.example.StadiumBooking.DataModel.Admin;
import com.example.StadiumBooking.DataModel.Stadium;
import com.example.StadiumBooking.DataModel.StadiumManager;
import com.example.StadiumBooking.repositeries.ScheduleRepo;
import com.example.StadiumBooking.repositeries.StadiumManagerRepo;
import com.example.StadiumBooking.repositeries.StadiumRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin

public class StadiumController {
    @Autowired
    StadiumRepo stadiumRepo;
    @Autowired
    ScheduleRepo scheduleRepo;

    @PostMapping("/addStadium")
    public ResponseEntity<String> addStadium(@RequestBody Stadium stadium){
        List<Stadium> availableStadiumList=stadiumRepo.findAll();
        if (availableStadiumList.stream().anyMatch(value->value.getName().equals(stadium.getName()))){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("there is already a stadium with given name");
        }
        stadiumRepo.save(stadium);
        return ResponseEntity.status(HttpStatus.CREATED).body("Stadium added successfully");
    }
    @GetMapping("/getStadiums")
    public ResponseEntity<List<Stadium> > getAllStadiums(){
        List<Stadium> allStadiums=stadiumRepo.findAll();
        if (allStadiums.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allStadiums);
    }
    @GetMapping("/getStadium/{stadiumName}")
    public ResponseEntity<?> getStadiumByName(@PathVariable String stadiumName){
        Stadium stadium=stadiumRepo.findByName(stadiumName);
        if(stadium==null){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stadium with name "+stadiumName+" does not exists");
        }
       return ResponseEntity.ok(List.of(stadium));
    }
    @DeleteMapping("/deleteStadium/{stadiumName}")
    public ResponseEntity<?> deleteStadiumByName(@PathVariable String stadiumName){
        Stadium stadium=stadiumRepo.findByName(stadiumName);
        Map<String,String > response=new HashMap<>();
        if(stadium==null){
            response.put("message","Stadium with name "+stadiumName+" does not exists");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if(!scheduleRepo.findByStadiumName(stadiumName).isEmpty()){
            response.put("message","game scheduled can't delete this stadium with name "+stadiumName);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        stadiumRepo.delete(stadium);
        response.put("message","stadium "+stadiumName +"deleted successfully");
           return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/getStadium/{stadiumName}")
    public ResponseEntity<?>editStadiumByName(@PathVariable String stadiumName, @RequestBody Stadium newStadium){
        Stadium stadium=stadiumRepo.findByName(stadiumName);
        if(stadium==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stadium with name "+stadiumName+" does not exists");
        }
       // BeanUtils.copyProperties(newStadium,stadium);
        if(!newStadium.getName().equals(stadiumName)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("stadium name can't be updated");
        }
        stadium.setStadiumType(newStadium.getStadiumType());
        stadium.setCapacity(newStadium.getCapacity());
        stadium.setLocation(newStadium.getLocation());
        stadium.setImageUrl(newStadium.getImageUrl());
        stadiumRepo.save(stadium);
        return ResponseEntity.ok(stadium);
    }

}
