package com.example.StadiumBooking.DataModel;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.Timestamp;
import java.time.Instant;
import java.util.List;
@Document(collection = "Bookings")
@Data
public class Bookings {
    @Id
    private String id;
    private String customerEmail;
    private String  stadiumName;
    private Schedule schedule;
    private Instant bookingTime;
    private List<String> seatNumbers;
    private  int no_of_seats;
    private int totalAmount;
    private String bookingStatus;
}
