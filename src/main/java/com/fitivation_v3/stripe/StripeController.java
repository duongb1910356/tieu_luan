package com.fitivation_v3.stripe;

import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.security.service.UserDetailsImpl;
import com.fitivation_v3.stripe.dto.CardDto;
import com.fitivation_v3.stripe.dto.PersonStripeDto;
import com.fitivation_v3.user.User;
import com.fitivation_v3.user.UserService;
import com.fitivation_v3.user.dto.UpdateUserDto;
import com.google.gson.Gson;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Person;
import jakarta.annotation.security.PermitAll;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/payment")
public class StripeController {

  @Autowired
  private StripeService stripeService;

  @Autowired
  private UserService userService;

  @Autowired
  private ModelMapper mapper;

  @PostMapping("/create_connect_account")
  public ResponseEntity<?> createConnectAccount() throws StripeException {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = mapper.map(userDetails, User.class);

    Account account = stripeService.createConnectAccount();
    user.setAccountIdStripe(account.getId());
    userService.updateUserById(user.getId(),
        UpdateUserDto.builder().accountIdStripe(user.getAccountIdStripe()).build());

    AccountLink accountLink = stripeService.createAccountLink(account.getId());
    System.out.println("AccountLink: " + accountLink.getUrl());

    Gson gson = new Gson();
    String accountJSON = gson.toJson(accountLink);
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(accountJSON);
  }

  @PostMapping("/create_customer")
  public ResponseEntity<?> createCustomer() throws StripeException {
    Customer customer = stripeService.createCustomer();
    Gson gson = new Gson();
    String customerJSON = gson.toJson(customer);

    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(customerJSON);
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

  @PostMapping("/create_payment_intent")
  public ResponseEntity<?> createPaymentIntent(@RequestParam long amount,
      @RequestParam String currency, @RequestParam String customerId,
      @RequestParam String timeRegister) {
    System.out.println("amout " + amount + ", " + currency);
    try {
      PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount, currency, customerId,
          timeRegister);
      Gson gson = new Gson();
      String paymentIntentJson = gson.toJson(paymentIntent);

      // Trả về JSON trong phản hồi HTTP
//      return new ResponseEntity<>(paymentIntentJson, HttpStatus.OK);
      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(paymentIntentJson);
    } catch (StripeException e) {
      return ResponseEntity.status(500).body("Failed to create Payment Intent: " + e.getMessage());
    }
//    return ResponseEntity.ok("ok pay");
  }

  @PostMapping("/create_ephemeral_key")
  public ResponseEntity<?> createEphemeralKey(@RequestParam String customerId) {
    try {
      System.out.println("da goi " + customerId);

      String ephemeralKey = stripeService.createEphemeralKey(customerId);
      System.out.println("da qua");
      Gson gson = new Gson();
      String ephemeralKeyJson = gson.toJson(ephemeralKey);
      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(ephemeralKeyJson);
    } catch (StripeException e) {
      return ResponseEntity.status(500).body("Failed to create ephemeral key: " + e.getMessage());
    }
  }

  @PostMapping("/webhook")
  public ResponseEntity<?> webhook(@RequestBody String payload,
      @RequestHeader("Stripe-Signature") String sigHeader) {
    try {
      System.out.println("da vao controller");
      stripeService.webhookListen(payload, sigHeader);

      return ResponseEntity.ok("Webhook success");
    } catch (Exception ex) {
      System.out.println("Error webhook: " + ex);
      throw new BadRequestException("Error webhook: " + ex);
    }
  }

  @GetMapping("/retrieve/payment_intent/{paymentId}")
  public ResponseEntity<?> retrievePaymentIntent(@PathVariable String paymentId) {
    try {
      PaymentIntent paymentIntent = stripeService.retrievePaymentItent(paymentId);

      Gson gson = new Gson();
      String dataJson = gson.toJson(paymentIntent);

      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(dataJson);
    } catch (Exception ex) {
      System.out.println("Error retrieve payment intent: " + ex);
      throw new BadRequestException("Error retrieve payment intent: " + ex);
    }
  }
}
