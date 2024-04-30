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
import java.util.*;

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
        Optional<Stadium> stadium=stadiumRepo.findById(stadiumId);
        if(stadium.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Optional<Schedule> existingSchedule=scheduleRepo.findById(bookings.getScheduleId());
        if(existingSchedule.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no game scheduled ");
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
        if(existingSchedule.get().getBookedSeats()==0){
            existingSchedule.get().setBookedSeats(bookings.getNo_of_seats());
        }
        else{
            existingSchedule.get().setBookedSeats(existingSchedule.get().getBookedSeats()+bookings.getNo_of_seats());
        }
        existingSchedule.get().setAvailableSeats(existingSchedule.get().getAvailableSeats()-bookings.getNo_of_seats());
       scheduleRepo.save(existingSchedule.get());
        return ResponseEntity.ok(bookings);

    }

    @DeleteMapping("delete/{bookingId}")
    public ResponseEntity<?> deleteBookings(@PathVariable String bookingId){
        Optional<Bookings> bookings= bookingsRepo.findById(bookingId);
        if(bookings.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("stadium does not exist");
        }
        Optional<Schedule> schedule=scheduleRepo.findById(bookings.get().getScheduleId());
        Stadium existingStadium=stadiumRepo.findByName(bookings.get().getStadiumName());
        existingStadium.setCapacity(existingStadium.getCapacity()+bookings.get().getNo_of_seats());
        if(schedule.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no games scheduled");
        }
        schedule.get().setAvailableSeats(schedule.get().getAvailableSeats()+bookings.get().getNo_of_seats());
        schedule.get().setBookedSeats(schedule.get().getBookedSeats()-bookings.get().getNo_of_seats());
        scheduleRepo.save(schedule.get());
        bookingsRepo.delete(bookings.get());
        return ResponseEntity.status(HttpStatus.OK).body("booking cancelled");

    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<List<Bookings>> getAllBookings(){
        List<Bookings> bookingsList=bookingsRepo.findAll();
        return ResponseEntity.ok(bookingsList);
    }

    @GetMapping("/getBookingsByMatch/{scheduleId}")
    public ResponseEntity<?> getBookingsByMatch(@PathVariable String scheduleId){
        List<Bookings> getAllBookings=bookingsRepo.findByScheduleId(scheduleId);
        if(getAllBookings.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no bookings for this game");
        }
        return ResponseEntity.ok(getAllBookings);

    }
}
