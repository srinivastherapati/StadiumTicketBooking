package com.example.StadiumBooking.repositeries;


import com.example.StadiumBooking.DataModel.Stadium;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StadiumRepo extends MongoRepository<Stadium,String> {
    Stadium findByName(String stadiumName);
}
