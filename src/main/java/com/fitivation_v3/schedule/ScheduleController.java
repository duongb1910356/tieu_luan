package com.fitivation_v3.schedule;

import com.fitivation_v3.review.Review;
import com.fitivation_v3.schedule.dto.CreateScheduleDto;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

  @Autowired
  private ScheduleService scheduleService;

  @PostMapping("/create/facility/{facilityId}")
  public ResponseEntity<?> createSchedule(@PathVariable ObjectId facilityId,
      @RequestBody CreateScheduleDto createScheduleDto) {
    Schedule schedule = scheduleService.createSchedule(facilityId, createScheduleDto);
    return new ResponseEntity<Schedule>(schedule, HttpStatus.OK);
  }

  @GetMapping("/get/facility/{facilityId}")
  public ResponseEntity<?> createSchedule(@PathVariable ObjectId facilityId) {
    Schedule schedule = scheduleService.getScheduleByFacilityId(facilityId);
    return new ResponseEntity<Schedule>(schedule, HttpStatus.OK);
  }
}
