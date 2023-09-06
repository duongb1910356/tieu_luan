package com.fitivation_v3.stripe;

import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.security.service.UserDetailsImpl;
import com.fitivation_v3.stripe.dto.CardDto;
import com.fitivation_v3.stripe.dto.PaymentItentDto;
import com.fitivation_v3.user.User;
import com.fitivation_v3.user.UserService;
import com.fitivation_v3.user.dto.UpdateUserDto;
import com.fitivation_v3.user.dto.UserDto;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.checkout.Session;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.AccountUpdateParams;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

  @Value("${STRIPE_SECRET_KEY}")
  private String secretKey;

  @Value("${STRIPE_PUBLIC_KEY}")
  private String publicKey;

  @Autowired
  private UserService userService;

  Gson gson = new Gson();


  @Autowired
  private ModelMapper mapper;

  @PostConstruct
  public void init() {
    Stripe.apiKey = secretKey;
  }

  public Account createConnectAccount() throws StripeException {
    try {

      UserDetailsImpl userDetails =
          (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      User user = mapper.map(userDetails, User.class);

      AccountCreateParams params =
          AccountCreateParams.builder()
              .setCountry("US")
              .setType(AccountCreateParams.Type.EXPRESS)
              .setCapabilities(
                  AccountCreateParams.Capabilities.builder()
                      .setCardPayments(
                          AccountCreateParams.Capabilities.CardPayments.builder()
                              .setRequested(true)
                              .build()
                      )
                      .setTransfers(
                          AccountCreateParams.Capabilities.Transfers.builder().setRequested(true)
                              .build()
                      )
                      .build()
              )
              .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
              .build();

      Account account = Account.create(params);

      System.out.println("ID của tài khoản kết nối: " + account.getId());
      System.out.println("Loại tài khoản kết nối: " + account.getType());
      System.out.println("Quốc gia: " + account.getCountry());

      return account;
    } catch (StripeException ex) {
      System.out.println("Error create stripe connect account: " + ex);
      throw new BadRequestException("Error create stripe connect account");
    }
  }

  public AccountLink createAccountLink(String connectAccountId) throws StripeException {
    try {
      AccountLinkCreateParams params =
          AccountLinkCreateParams.builder()
              .setAccount(connectAccountId)
              .setRefreshUrl("https://example.com/reauth")
              .setReturnUrl("https://example.com/return")
              .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
              .build();

      AccountLink accountLink = AccountLink.create(params);

      return accountLink;
    } catch (StripeException ex) {
      System.out.println("Error create account link: " + ex);
      throw new BadRequestException("Error create account link");
    }
  }

  public Account updateAccountStripe(String accountId) throws StripeException {
    try {
      Account account = Account.retrieve(accountId);
      System.out.println("Retrieve accountId: " + account.getId());

      AccountUpdateParams params = AccountUpdateParams.builder()
          .setCapabilities(
              AccountUpdateParams.Capabilities.builder()
                  .setCardPayments(
                      AccountUpdateParams.Capabilities.CardPayments.builder()
                          .setRequested(true)
                          .build()
                  )
                  .setTransfers(
                      AccountUpdateParams.Capabilities.Transfers.builder().setRequested(true)
                          .build()
                  )
                  .build()
          )
          .build();

      Account updatedAccount = account.update(params);

      return updatedAccount;

    } catch (StripeException ex) {
      System.out.println("Error update connect account stripe: " + ex);
      throw new BadRequestException("Error update connect account stripe");
    }
  }

  public Session createSession() throws StripeException {
    try {

      List<Object> lineItems = new ArrayList<>();
      Map<String, Object> lineItem1 = new HashMap<>();
      lineItem1.put("price", "price_H5ggYwtDq4fbrJ");
      lineItem1.put("quantity", 2);
      lineItems.add(lineItem1);
      Map<String, Object> params = new HashMap<>();
      params.put(
          "success_url",
          "https://example.com/success"
      );
      params.put("line_items", lineItems);
      params.put("mode", "payment");

      Session session = Session.create(params);

      return session;
    } catch (StripeException ex) {
      System.out.println("Error create session checkout: " + ex);
      throw new BadRequestException("Error create session checkout");
    }
  }

  public String createPaymentIntent(String customerId, String connectAccountId,
      PaymentItentDto paymentItentDto) throws StripeException {
    Customer customer = Customer.retrieve(customerId);

    EphemeralKeyCreateParams ephemeralKeyParams =
        EphemeralKeyCreateParams.builder()
            .setStripeVersion("2023-08-16")
            .setCustomer(customer.getId())
            .build();

    EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyParams);

    List<String> paymentMethodTypes = new ArrayList<String>();
    paymentMethodTypes.add("bancontact");
    paymentMethodTypes.add("card");
    paymentMethodTypes.add("ideal");
    paymentMethodTypes.add("klarna");
    paymentMethodTypes.add("sepa_debit");
    paymentMethodTypes.add("sofort");

    PaymentIntentCreateParams paymentIntentParams =
        PaymentIntentCreateParams.builder()
            .setAmount(paymentItentDto.getAmount())
            .setCurrency("vnd")
            .setCustomer(customer.getId())
            .addAllPaymentMethodType(paymentMethodTypes)
            .setApplicationFeeAmount(paymentItentDto.getApplicationFeeAmount())
            .setTransferData(
                PaymentIntentCreateParams.TransferData.builder()
                    .setDestination(connectAccountId)
                    .build())
            .build();

    PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);

    Map<String, String> responseData = new HashMap();
    responseData.put("paymentIntent", paymentIntent.getClientSecret());
    responseData.put("ephemeralKey", ephemeralKey.getSecret());
    responseData.put("customer", customer.getId());
    responseData.put("publishableKey", publicKey);

    return gson.toJson(responseData);
  }

  public Customer createCustomer() throws StripeException {
    try {
      UserDetailsImpl userDetails =
          (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      Optional<User> user = userService.getUserById(userDetails.getId());

      Map<String, Object> params = new HashMap<>();
      params.put(
          "description",
          "My Test Customer"
      );

      Map<String, Object> address = new HashMap<>();
      if (user.isPresent() && user.get().getAddress() != null) {
        address.put("city", user.get().getAddress().getProvince());
        address.put("line1", user.get().getAddress().getStreet());
        address.put("line2", user.get().getAddress().getWard());

      }

      address.put("country", "VN");
      params.put("address", address);

      params.put("name", user.get().getDisplayName());
      params.put("phone", user.get().getPhone());

      Customer customer = Customer.create(params);

      userService.updateUserById(userDetails.getId(),
          UpdateUserDto.builder().customerIdStripe(customer.getId()).build());

      return customer;

    } catch (StripeException ex) {
      System.out.println("Error create customer: " + ex);
      throw new BadRequestException("Error create customer");
    }
  }

  public PaymentMethod createPaymentMethodAndAttachToCustomer(String customerId)
      throws StripeException {
    try {
      PaymentMethod paymentMethod =
          PaymentMethod.retrieve(
              "pm_card_visa"
          );

      Map<String, Object> params = new HashMap<>();
      params.put("customer", customerId);

      PaymentMethod updatedPaymentMethod =
          paymentMethod.attach(params);

      return updatedPaymentMethod;
    } catch (StripeException ex) {
      System.out.println("Error create payment method and attach: " + ex);
      throw new BadRequestException("Error create payment method and attach");
    }
  }

}
