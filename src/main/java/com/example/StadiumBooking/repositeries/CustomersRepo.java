package com.example.StadiumBooking.repositeries;

import com.example.StadiumBooking.DataModel.Customers;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomersRepo extends MongoRepository<Customers,String> {
    Customers findByEmail(String email);
}
