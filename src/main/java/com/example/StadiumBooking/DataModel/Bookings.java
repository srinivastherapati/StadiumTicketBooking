package com.example.StadiumBooking.DataModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
@Document(collection = "Bookings")
@Data
public class Bookings {
    @Id
    private String id;
    private String customerEmail;
    private String  stadiumName;
    private Instant bookingTime;
    private  int no_of_seats;
    private int totalAmount;
    private String bookingStatus;
    private String seatType;
    private String gameTitle;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date startTime;
}
