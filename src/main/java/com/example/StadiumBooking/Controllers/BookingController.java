package com.example.StadiumBooking.Controllers;

import com.example.StadiumBooking.DataModel.Bookings;
import com.example.StadiumBooking.DataModel.Schedule;
import com.example.StadiumBooking.DataModel.SeatType;
import com.example.StadiumBooking.DataModel.Stadium;
import com.example.StadiumBooking.repositeries.BookingsRepo;
import com.example.StadiumBooking.repositeries.ScheduleRepo;
import com.example.StadiumBooking.repositeries.StadiumRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@RestController
@RequestMapping("/bookings")
@CrossOrigin
public class BookingController {
    @Autowired
    private StadiumRepo stadiumRepo;
    @Autowired
    private BookingsRepo bookingsRepo;
    @Autowired
    private ScheduleRepo scheduleRepo;

    @PostMapping("/book/{stadiumId}")
    public ResponseEntity<?> startBooking(@PathVariable String stadiumId, @RequestParam String gameTitle
            , @RequestBody Bookings bookings){
        Date startTimeDate = null;
        System.out.println("================="+bookings.getStartTime());
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            startTimeDate = dateFormat.parse(String.valueOf(bookings.getStartTime()));
            System.out.println("=============="+startTimeDate);
        } catch (ParseException | java.text.ParseException e) {
            // Handle parsing error
            return ResponseEntity.badRequest().body("Invalid startTime format: " + bookings.getStartTime());
        }
        Optional<Stadium> stadium=stadiumRepo.findById(stadiumId);
        if(stadium.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Schedule schedule=scheduleRepo.findByStadiumNameAndGameTitleAndStartTime(stadium.get().getName(),gameTitle,startTimeDate);
        if(schedule==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no games with name " + gameTitle +" at time " +startTimeDate);
        }
        SeatType seatType=SeatType.valueOf(bookings.getSeatType());
        switch (seatType) {
            case SILVER:
                bookings.setTotalAmount(300);
                break;
            case GOLD:
                bookings.setTotalAmount(500);
                break;
            case PREMIUM:
                bookings.setTotalAmount(700);
                break;
            default:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("please select seating type");

        }
        bookings.setStadiumName(stadium.get().getName());
        bookings.setBookingTime(Instant.now());
        bookings.setTotalAmount(bookings.getTotalAmount()* bookings.getNo_of_seats());
        bookings.setBookingStatus("booked");
       // bookings.setStartTime(startTime);
        bookingsRepo.save(bookings);
       // stadium.get().setCapacity(stadium.get().getCapacity()- bookings.getNo_of_seats());
        schedule.setBookedSeats(bookings.getNo_of_seats());
        schedule.setAvailableSeats(schedule.getAvailableSeats()-bookings.getNo_of_seats());
        stadiumRepo.save(stadium.get());
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("delete/{bookingId}")
    public ResponseEntity<?> deleteBookings(@PathVariable String bookingId){
        Optional<Bookings> bookings= bookingsRepo.findById(bookingId);
        if(bookings.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("stadium does not exist");
        }
        bookingsRepo.delete(bookings.get());
        Stadium existingStadium=stadiumRepo.findByName(bookings.get().getStadiumName());
        existingStadium.setCapacity(existingStadium.getCapacity()+bookings.get().getNo_of_seats());
        stadiumRepo.save(existingStadium);
        return ResponseEntity.status(HttpStatus.OK).body("booking cancelled");

    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<List<Bookings>> getAllBookings(){
        List<Bookings> bookingsList=bookingsRepo.findAll();
        return ResponseEntity.ok(bookingsList);
    }

    @GetMapping("getBookingsByStadium/{stadiumName}")
    public ResponseEntity<?> getBookingsByName(@PathVariable String stadiumName){
       List<Bookings> bookings= bookingsRepo.findByStadiumName(stadiumName);
       if(bookings.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no bookings for this stadium");
       }
       return ResponseEntity.ok(bookings);
    }

//    @GetMapping("/getBookingsByMatch/{scheduleId}")
//    public ResponseEntity<?> getBookingsByMatch(@PathVariable String scheduleId){
//
//    }
}
