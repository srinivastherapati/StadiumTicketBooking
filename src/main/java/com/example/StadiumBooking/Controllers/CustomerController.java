package com.example.StadiumBooking.Controllers;

import com.example.StadiumBooking.DataModel.Customers;
import com.example.StadiumBooking.repositeries.CustomersRepo;
import com.example.StadiumBooking.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@CrossOrigin
public class CustomerController {
    @Autowired
    private CustomersRepo customersRepo;
    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> customerRegister(@RequestBody Customers customers){
        if(customers.getEmail()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email is required");
        }
        if(customers.getPhoneNumber()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("phone number is required");
        }
        if(customers.getPassword()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("password is required");
        }
        if(customerService.isUserExist(customers.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("customer already registered with email "+customers.getEmail());
        }
        customersRepo.save(customers);
        return ResponseEntity.status(HttpStatus.CREATED).body("customer registered successfully");
    }
}
