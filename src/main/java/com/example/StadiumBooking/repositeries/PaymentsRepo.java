package com.example.StadiumBooking.repositeries;

import com.example.StadiumBooking.DataModel.Payments;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentsRepo extends MongoRepository<Payments,String > {


}
