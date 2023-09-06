package com.fitivation_v3.stripe;

import com.fitivation_v3.stripe.dto.CardDto;
import com.fitivation_v3.stripe.dto.PersonStripeDto;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class StripeController {

  @Autowired
  private StripeService stripeService;

  @PostMapping("/create_connect_account")
  public ResponseEntity<?> createConnectAccount() throws StripeException {
    Account account = stripeService.createConnectAccount();
    AccountLink accountLink = stripeService.createAccountLink(account.getId());
    System.out.println("AccountLink: " + accountLink.getUrl());

    String accountJSON = account.toJson();
    return new ResponseEntity<>(accountJSON, HttpStatus.OK);
  }

  @PostMapping("/create_customer")
  public ResponseEntity<?> createCustomer() throws StripeException {
    Customer customer = stripeService.createCustomer();
    String customerJSON = customer.toJson();

    return new ResponseEntity<>(customerJSON, HttpStatus.OK);
  }

  @PostMapping("/update_customer/{customerId}/payment_method")
  public ResponseEntity<?> createPaymentMethodAndAttachToCustomer(@PathVariable String customerId)
      throws StripeException {
    PaymentMethod paymentMethod = stripeService.createPaymentMethodAndAttachToCustomer(customerId);
    String paymentMethodJSON = paymentMethod.toJson();

    return new ResponseEntity<>(paymentMethodJSON, HttpStatus.OK);
  }

  @PatchMapping("/update_connect_account/{accountId}")
  public ResponseEntity<?> updateConnectAccount(@PathVariable String accountId)
      throws StripeException {
    System.out.println("Account id: " + accountId);
    Account account = stripeService.updateAccountStripe(accountId);
    String accountJSON = account.toJson();
    return new ResponseEntity<>(accountJSON, HttpStatus.OK);
  }

}
