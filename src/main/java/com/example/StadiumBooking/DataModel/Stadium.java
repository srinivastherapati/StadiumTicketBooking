package com.example.StadiumBooking.DataModel;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Stadium")
@Data
public class Stadium {
    @Id
    private String id;
    private String name;
    private String stadiumType;
    private String  location;
    private int capacity;

}


