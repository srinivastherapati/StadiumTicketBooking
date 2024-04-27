package com.example.StadiumBooking.Controllers;

import com.example.StadiumBooking.DataModel.Payments;
import com.example.StadiumBooking.repositeries.PaymentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentsController {

    @Autowired
    PaymentsRepo paymentsRepo;

    @PostMapping("/pay")
    public ResponseEntity<?> makePayment(@RequestBody Payments payments){
        if(payments.getCardNumber()==null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("card number is required");
        }
        if(payments.getCvv()==null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cvv is required");
        }
        if(payments.getExpiryDate()==null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("card expiry date is required");
        }
        if(payments.getNameOnCard()==null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("name on card is required");
        }
        paymentsRepo.save(payments);
        return  ResponseEntity.status(HttpStatus.OK).body("Payment successful");
    }

}
