package com.example.StadiumBooking.DataModel;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "stadiumManager")
public class StadiumManager {
    @Id
    private String id;
    private String stadiumName;
    private String name;
    private String email;
    private String phoneNumber;
    private  String password;
    private boolean approved;
    private String role;
}
