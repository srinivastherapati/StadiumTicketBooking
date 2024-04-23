package com.example.StadiumBooking.repositeries;

import com.example.StadiumBooking.DataModel.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepo extends MongoRepository<Admin,String> {
}
