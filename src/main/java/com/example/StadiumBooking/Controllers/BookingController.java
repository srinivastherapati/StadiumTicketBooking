package com.example.StadiumBooking.Controllers;

import com.example.StadiumBooking.DataModel.Bookings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @PostMapping("/book")
    public ResponseEntity<?> startBooking(@RequestBody Bookings bookings){
        return null;
    }
}
