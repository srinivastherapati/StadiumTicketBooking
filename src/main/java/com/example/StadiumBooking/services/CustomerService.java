package com.example.StadiumBooking.services;

import com.example.StadiumBooking.DataModel.Customers;
import com.example.StadiumBooking.repositeries.CustomersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomersRepo customersRepo;
    public boolean isUserExist(String email) {
        Customers existingCustomer = customersRepo.findByEmail(email);
        return existingCustomer!=null;
    }
}
