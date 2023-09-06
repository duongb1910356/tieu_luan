package com.fitivation_v3.review;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, ObjectId> {

  public List<Review> findByFacilityId(ObjectId id);
}
