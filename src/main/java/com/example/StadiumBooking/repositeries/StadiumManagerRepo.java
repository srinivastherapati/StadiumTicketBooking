package com.example.StadiumBooking.repositeries;

import com.example.StadiumBooking.DataModel.Bookings;
import com.example.StadiumBooking.DataModel.StadiumManager;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StadiumManagerRepo extends MongoRepository<StadiumManager,String> {
StadiumManager findByEmail(String email);
List<StadiumManager> findByApprovedFalse();
StadiumManager findByStadiumName(String stadiumName);
}
