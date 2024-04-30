package com.example.StadiumBooking.repositeries;

import com.example.StadiumBooking.DataModel.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ScheduleRepo extends MongoRepository<Schedule,String> {

   // Schedule findByStadiumName(String stadiumName);
    List<Schedule> findByStadiumName(String stadiumName);
    Schedule findByStadiumNameAndGameTitleAndStartTime(String stadiumName, String gameTitle,Date startTime);
    @Query("{ 'stadiumName' : ?0, " +
            "$or: [ " +
            "{ 'startTime': { $gte: ?1, $lte: ?2 } }, " +
            "{ 'endTime': { $gte: ?3, $lte: ?4 } } " +
            "], " +
            "'_id': { $ne: ?5 } }")
    List<Schedule> findConflictingSchedules(String stadiumName,
                                            LocalDateTime startRange1, LocalDateTime endRange1,
                                            LocalDateTime startRange2, LocalDateTime endRange2,
                                            String excludeScheduleId);
}
