package com.example.StadiumBooking.DataModel;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Payments")
@Data
public class Payments {

    @Id
    private String id;
    private String cardNumber;
    private String cvv;
    private String nameOnCard;
    private String expiryDate;
}
