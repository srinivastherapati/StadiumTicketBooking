package com.example.StadiumBooking.services;

import com.example.StadiumBooking.DataModel.StadiumManager;
import com.example.StadiumBooking.repositeries.StadiumManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StadiumManagerService {
    @Autowired
    private StadiumManagerRepo stadiumManagerRepo;
    public boolean isManagerExists(String email){
        StadiumManager stadiumManager=stadiumManagerRepo.findByEmail(email);
        return stadiumManager!=null;
    }
    public boolean isAuthorized(String stadiumManagerEmail) {
        StadiumManager existingStadiumManager = stadiumManagerRepo.findByEmail(stadiumManagerEmail);
       if(existingStadiumManager==null){
           return false;
       }
       return existingStadiumManager.isApproved();
    }
    public List<StadiumManager> getPendingRequest(){
        return stadiumManagerRepo.findByApprovedFalse();
    }

}
