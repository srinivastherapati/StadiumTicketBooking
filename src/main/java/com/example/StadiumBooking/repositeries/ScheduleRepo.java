package com.example.StadiumBooking.repositeries;

import com.example.StadiumBooking.DataModel.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRepo extends MongoRepository<Schedule,String> {

    Schedule findByStadiumName(String stadiumName);
}
