package com.example.StadiumBooking.DataModel;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Admin")
@Data
public class Admin {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
}
