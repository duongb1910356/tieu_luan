package com.fitivation_v3.subscription;

import com.fitivation_v3.bill.Bill;
import com.fitivation_v3.exception.BadRequestException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

  @Autowired
  private SubscriptionService subscriptionService;

  @GetMapping("/me")
  public ResponseEntity<?> getSubscriptionOfMe() {
    try {
      List<Subscription> subscriptions = subscriptionService.getSubscriptionOfMe();
      return new ResponseEntity<>(subscriptions, HttpStatus.OK);

    } catch (Exception ex) {
      System.out.println("Error get subscriptions: " + ex);
      throw new BadRequestException("Error get subscriptions");
    }
  }

}
