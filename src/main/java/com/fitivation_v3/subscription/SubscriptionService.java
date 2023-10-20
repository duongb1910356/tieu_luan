package com.fitivation_v3.subscription;

import com.fitivation_v3.bill.Bill;
import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.security.service.UserDetailsImpl;
import com.fitivation_v3.user.User;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private ModelMapper mapper;

  public Subscription createSubscription(Bill bill, User user) {
    Subscription subscription = Subscription.builder().packageFacility(bill.getItem()
        .getPackageFacility()).user(user).build();

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
}
