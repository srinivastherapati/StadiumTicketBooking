package com.example.StadiumBooking.Controllers;

import com.example.StadiumBooking.DataModel.StadiumManager;
import com.example.StadiumBooking.repositeries.StadiumManagerRepo;
import com.example.StadiumBooking.repositeries.StadiumRepo;
import com.example.StadiumBooking.services.StadiumManagerService;
import com.example.StadiumBooking.services.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/stadium-managers")
@CrossOrigin
public class StadiumManagerController {

    @Autowired
    private StadiumManagerRepo stadiumManagerRepository;
    @Autowired
    private StadiumRepo stadiumRepo;
    @Autowired
    private StadiumService stadiumService;
    @Autowired
    private StadiumManagerService stadiumManagerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerStadiumManager(@RequestBody StadiumManager stadiumManager) {
//        if(stadiumManager.getEmail().equals("admin") && stadiumManager.getPassword().equals("admin@123")){
//            stadiumManagerRepository.save(stadiumManager);
//            return ResponseEntity.status(HttpStatus.CREATED).body("admin created");
//        }
        Map<String , Object> response= new HashMap<>();
        StadiumManager existingManager=stadiumManagerRepository.findByEmail(stadiumManager.getEmail());
        if(existingManager!=null){
            response.put("message","manager already registered ");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        if(!stadiumService.IsStadiumExists(stadiumManager.getStadiumName())){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("stadium with name "+stadiumManager.getStadiumName() +" does not exists");
        }
        stadiumManagerRepository.save(stadiumManager);
        return ResponseEntity.ok("Stadium manager registration request sent. Waiting for admin approval.");
    }
    @PutMapping("/approve/{managerId}")
    public ResponseEntity<String> approveManagerRequest(@PathVariable String managerId) {
        Optional<StadiumManager> optionalManager = stadiumManagerRepository.findById(managerId);
        if (optionalManager.isPresent()) {
            StadiumManager manager = optionalManager.get();
            manager.setApproved(true);
            stadiumManagerRepository.save(manager);
            return ResponseEntity.ok("Stadium manager registration approved.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody StadiumManager stadiumManager){
        if(stadiumManager.getEmail().equals("admin") && stadiumManager.getPassword().equals("admin@123")){
            return ResponseEntity.ok(stadiumManager.getEmail());
        }
       StadiumManager isManagerExists= stadiumManagerRepository.findByEmail(stadiumManager.getEmail());
       if(isManagerExists==null){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("manager does not exists");
       }
       if(isManagerExists.getEmail().equals(stadiumManager.getEmail())
               && !isManagerExists.getPassword().equals(stadiumManager.getPassword())){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email password mismatch");
       }
       if(!isManagerExists.isApproved()){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("approval is pending can't login");
       }
       stadiumManager.setRole("manager");
       Map<String ,String> managerDetails= new HashMap<>();
       managerDetails.put("email",stadiumManager.getEmail());
       managerDetails.put("name",stadiumManager.getName());
       managerDetails.put("role",stadiumManager.getRole());
       return ResponseEntity.ok(managerDetails);
    }
    @PostMapping("/admin/approve")
    public ResponseEntity<?> getAllApprovalRequest(){
        List<StadiumManager> pendingApprovals=stadiumManagerService.getPendingRequest();
        return ResponseEntity.ok(pendingApprovals);
    }

}
