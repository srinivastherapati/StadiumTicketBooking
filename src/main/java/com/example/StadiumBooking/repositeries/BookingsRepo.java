package com.example.StadiumBooking.repositeries;

import com.example.StadiumBooking.DataModel.Bookings;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingsRepo extends MongoRepository<Bookings,String> {

    List<Bookings> findByStadiumName(String stadiumName);

}
