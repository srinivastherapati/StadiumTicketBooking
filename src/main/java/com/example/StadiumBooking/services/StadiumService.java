package com.example.StadiumBooking.services;

import com.example.StadiumBooking.DataModel.Schedule;
import com.example.StadiumBooking.DataModel.Stadium;
import com.example.StadiumBooking.DataModel.StadiumManager;
import com.example.StadiumBooking.repositeries.ScheduleRepo;
import com.example.StadiumBooking.repositeries.StadiumRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StadiumService {
    @Autowired
    private StadiumRepo stadiumRepo;
    @Autowired
    private ScheduleRepo scheduleRepo;
    public boolean IsStadiumExists(String stadiumName){
       Stadium stadium= stadiumRepo.findByName(stadiumName);
        return stadium != null;
    }


}
