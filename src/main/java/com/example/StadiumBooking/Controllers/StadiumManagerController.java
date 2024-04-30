package com.example.StadiumBooking.Controllers;

import com.example.StadiumBooking.DataModel.Customers;
import com.example.StadiumBooking.DataModel.Schedule;
import com.example.StadiumBooking.DataModel.Stadium;
import com.example.StadiumBooking.DataModel.StadiumManager;
import com.example.StadiumBooking.repositeries.CustomersRepo;
import com.example.StadiumBooking.repositeries.ScheduleRepo;
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
    @Autowired
    private CustomersRepo customersRepo;
    @Autowired
    private ScheduleRepo scheduleRepo;

    @PostMapping("/stadium-managers/register")
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
        if(stadiumService.IsStadiumExists(stadiumManager.getStadiumName())==null){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("stadium with name "+stadiumManager.getStadiumName() +" does not exists");
        }
        stadiumManager.setRole("manager");
        stadiumManagerRepository.save(stadiumManager);
        return ResponseEntity.ok("Stadium manager registration request sent. Waiting for admin approval.");
    }
    @PutMapping("/stadium-managers/approve/{managerId}")
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
    public ResponseEntity<?> login(@RequestBody StadiumManager loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Check if the provided credentials match the admin credentials
        if (email.equals("admin") && password.equals("admin@123")) {
            Map<String, String> adminDetails = new HashMap<>();
            adminDetails.put("email", email);
            adminDetails.put("role", "admin");
            return ResponseEntity.ok(adminDetails);
        }

        // Check if the provided credentials match a stadium manager
        StadiumManager stadiumManager = stadiumManagerRepository.findByEmail(email);
        if (stadiumManager != null) {
            // Check if the password matches
            if (!stadiumManager.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email password mismatch");
            }
            // Check if the manager is approved
            if (!stadiumManager.isApproved()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Approval is pending, cannot login");
            }
            // Return stadium manager details
            Map<String, String> managerDetails = new HashMap<>();
            managerDetails.put("email", stadiumManager.getEmail());
            managerDetails.put("name", stadiumManager.getName());
            managerDetails.put("role", "manager");
            return ResponseEntity.ok(managerDetails);
        }

        // Check if the provided credentials match a customer
        Customers customer = customersRepo.findByEmail(email);
        if (customer != null) {
            // Check if the password matches
            if (!customer.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email password mismatch");
            }
            // Return customer details
            Map<String, String> customerDetails = new HashMap<>();
            customerDetails.put("email", customer.getEmail());
            customerDetails.put("name", customer.getName());
            customerDetails.put("role", "customer");
            return ResponseEntity.ok(customerDetails);
        }

        // If no match found, return not found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
    }

    @PostMapping("/stadium-managers/admin/approved")
    public ResponseEntity<?> getAllApprovalRequest(){
        List<StadiumManager> pendingApprovals=stadiumManagerService.getPendingRequest();
        return ResponseEntity.ok(pendingApprovals);
    }

    @GetMapping("/stadium-managers/stadium/{stadiumName}/managers")
    public ResponseEntity<?> getStadiumManager(@PathVariable String stadiumName){
        Stadium existingStadium=stadiumRepo.findByName(stadiumName);
        if(existingStadium==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("stadium with name "+ stadiumName + " does not exists");
        }
        StadiumManager manager=stadiumManagerRepository.findByStadiumName(stadiumName);
        return ResponseEntity.ok(manager);
    }

    @GetMapping("/stadium-managers/stadium/{stadiumId}/approvals")
    public  ResponseEntity<?> getStadiumApprovals(@PathVariable String stadiumId){
        Optional<Stadium> existingStadium= stadiumRepo.findById(stadiumId);
        if(existingStadium.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("stadium does not exists");
        }
        StadiumManager stadiumManager= stadiumManagerRepository.findByStadiumName(existingStadium.get().getName());

            return ResponseEntity.ok(stadiumManager);
    }

    @PutMapping("/stadium-managers/stadium/{stadiumId}/approvals")
    public  ResponseEntity<?> editManagerApprovals(@PathVariable String stadiumId,@RequestBody StadiumManager stadiumManager){
        Optional<Stadium> existingStadium= stadiumRepo.findById(stadiumId);
        if(existingStadium.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("stadium does not exists");
        }
        StadiumManager manager=stadiumManagerRepository.findByStadiumName(existingStadium.get().getName());
        if(manager == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stadium manager does not exists for this stadium");
        }
        manager.setApproved(stadiumManager.isApproved());
        stadiumManagerRepository.save(manager);
        return ResponseEntity.ok(manager);
    }

    @GetMapping("/getMatches/{managerEmail}")
    public ResponseEntity<?> getMatchesByManagerEmail(@PathVariable String  managerEmail){
        StadiumManager stadiumManager= stadiumManagerRepository.findByEmail(managerEmail);
        if(stadiumManager==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("manager does not exists");
        }

        List<Schedule> allSchedules=scheduleRepo.findByStadiumName(stadiumManager.getStadiumName());
        if(allSchedules.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no matches scheduled");
        }
        return ResponseEntity.ok(allSchedules);

    }





}
