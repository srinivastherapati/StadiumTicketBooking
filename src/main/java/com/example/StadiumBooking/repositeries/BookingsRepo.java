package com.example.StadiumBooking.repositeries;

import com.example.StadiumBooking.DataModel.Bookings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingsRepo extends MongoRepository<Bookings,String> {

}
