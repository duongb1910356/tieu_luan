package com.fitivation_v3.bill;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends MongoRepository<Bill, ObjectId> {

  public Bill findByPaymentIntent(String paymentIntentId);

  public List<Bill> findByCustomerIdStripe(String customerIdStripe);

  public List<Bill> findByOwnerIdAndDateCreatedBetween(ObjectId ownerId, Date startDate,
      Date endDate);
}
