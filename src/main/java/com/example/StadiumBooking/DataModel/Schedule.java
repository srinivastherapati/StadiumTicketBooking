package com.example.StadiumBooking.DataModel;

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
    private Date startTime;
    private Date endTime;

}
