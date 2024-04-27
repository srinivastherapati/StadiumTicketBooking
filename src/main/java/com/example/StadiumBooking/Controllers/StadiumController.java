package com.example.StadiumBooking.Controllers;

import com.example.StadiumBooking.DataModel.Admin;
import com.example.StadiumBooking.DataModel.Stadium;
import com.example.StadiumBooking.DataModel.StadiumManager;
import com.example.StadiumBooking.repositeries.StadiumManagerRepo;
import com.example.StadiumBooking.repositeries.StadiumRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin

public class StadiumController {
    @Autowired
    StadiumRepo stadiumRepo;

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
       return ResponseEntity.ok(stadium);
    }
    @DeleteMapping("/deleteStadium/{stadiumName}")
    public ResponseEntity<String> deleteStadiumByName(@PathVariable String stadiumName){
        Stadium stadium=stadiumRepo.findByName(stadiumName);
        if(stadium==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stadium with name "+stadiumName+" does not exists");
        }

        stadiumRepo.delete(stadium);
           return ResponseEntity.status(HttpStatus.OK).body("Stadium with name "+stadiumName+" deleted successfully");
    }

}
