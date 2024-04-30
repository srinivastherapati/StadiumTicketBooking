package com.example.StadiumBooking.DataModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "Schedule")
@Data
public class Schedule {
    @Id
    private String id;
    private String stadiumName;
    private String  stadiumManagerEmail;
    private String gameTitle;
    private String no_of_teams;
    private String no_of_players_in_team;
    private String homeTeam;
    private String awayTeam;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date endTime;
    private int availableSeats;
    private int bookedSeats;

}
