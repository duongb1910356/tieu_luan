package com.fitivation_v3.schedule;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, ObjectId> {

  public Schedule findByFacility(ObjectId facilityId);
}
