package com.example.StadiumBooking.repositeries;

import com.example.StadiumBooking.DataModel.Stadium;
import com.example.StadiumBooking.DataModel.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface TimeSlotRepo extends MongoRepository<TimeSlot,String> {
   // List<TimeSlot> findByStadiumStadiumNameAndDate(Stadium stadium, Date date);
    List<TimeSlot> findByDate(Date date);
    List<TimeSlot> findByStadiumNameAndDate(String stadiumName, Date date);
}
