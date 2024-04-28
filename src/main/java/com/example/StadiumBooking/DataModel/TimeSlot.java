package com.example.StadiumBooking.DataModel;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import java.time.Instant;
import java.util.Date;

@Document(collection = "Time_slot")
@Data
public class TimeSlot {
    @Id
    private String id;
    private Date date;
    private Instant startTime;
    private Instant endTime;
    private String  stadiumName;


}
