package com.fitivation_v3.subscription;

import com.fitivation_v3.user.User;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, ObjectId> {

  public List<Subscription> findByUser(User user);

  public Long countByFacilityId(ObjectId facilityId);

}
