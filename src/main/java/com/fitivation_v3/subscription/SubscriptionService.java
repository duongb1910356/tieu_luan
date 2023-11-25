package com.fitivation_v3.subscription;

import com.fitivation_v3.bill.Bill;
import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.review.Review;
import com.fitivation_v3.review.dto.ReviewSummary;
import com.fitivation_v3.security.service.UserDetailsImpl;
import com.fitivation_v3.subscription.dto.SubscriptionStatisticTerm;
import com.fitivation_v3.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;

@Service
public class SubscriptionService {

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private ModelMapper mapper;

  @Autowired
  private MongoTemplate mongoTemplate;

  public Subscription createSubscription(Bill bill, User user) {
    Subscription subscription = Subscription.builder().packageFacility(bill.getItem()
            .getPackageFacility()).user(user).timeRegister(bill.getTimeRegister())
        .facilityId(bill.getItem().getPackageFacility().getFacility().getId()).build();

    subscription.caclutateExpireDay();

    return subscriptionRepository.save(subscription);
  }

  public List<Subscription> getSubscriptionOfMe() {
    try {
      UserDetailsImpl userDetails =
          (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      User user = mapper.map(userDetails, User.class);

      List<Subscription> subscriptions = subscriptionRepository.findByUser(user);
      return subscriptions;
    } catch (Exception ex) {
      System.out.println("Can not get list bills: " + ex);
      throw new BadRequestException("Can not get list bills");
    }
  }

  public List<SubscriptionStatisticTerm> statisticsSubscriptionWithTerm(ObjectId packageFacility) {
    try {
      Aggregation aggregation = Aggregation.newAggregation(
          Aggregation.match(Criteria.where("packageFacility").is(packageFacility)),
          Aggregation.group("timeRegister").count().as("count"),
          Aggregation.project(Fields.from(
              Fields.field("timeRegister", "_id"),
              Fields.field("count", "count")
          ))
      );

      AggregationResults<SubscriptionStatisticTerm> results = mongoTemplate.aggregate(aggregation,
          Subscription.class,
          SubscriptionStatisticTerm.class);
      List<SubscriptionStatisticTerm> summaries = new ArrayList<>(results.getMappedResults());

      return summaries;

    } catch (Exception ex) {
      System.out.println("Can not get list statisticsSubscriptionWithTerm: " + ex);
      throw new BadRequestException("Can not get list statisticsSubscriptionWithTerm");
    }
  }
}
