package com.example.StadiumBooking.repositeries;

import com.example.StadiumBooking.DataModel.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScheduleRepo extends MongoRepository<Schedule,String> {

   // Schedule findByStadiumName(String stadiumName);
    List<Schedule> findByStadiumName(String stadiumName);
}
