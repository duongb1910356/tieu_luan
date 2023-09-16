package com.fitivation_v3.schedule;

import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.facility.Facility;
import com.fitivation_v3.schedule.dto.CreateScheduleDto;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

  @Autowired
  private ScheduleRepository scheduleRepository;

  public Schedule createSchedule(ObjectId facilityId, CreateScheduleDto createScheduleDto) {
    try {
      Schedule schedule = Schedule.builder().facility(facilityId)
          .openTimes(createScheduleDto.getOpenTimes()).build();
      schedule = scheduleRepository.save(schedule);

      return schedule;
    } catch (Exception ex) {
      System.out.println("Fail create schedule: " + ex);
      throw new BadRequestException("Fail create schedule");
    }
  }

  public Schedule getScheduleByFacilityId(ObjectId facilityId) {
    try {
      Schedule schedule = scheduleRepository.findByFacility(facilityId);
      return schedule;
    } catch (Exception ex) {
      System.out.println("Fail get schedule: " + ex);
      throw new BadRequestException("Fail get schedule");
    }
  }
}
