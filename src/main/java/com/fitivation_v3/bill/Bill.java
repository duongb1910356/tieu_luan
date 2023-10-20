package com.fitivation_v3.bill;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fitivation_v3.cart.Cart;
import com.fitivation_v3.cart_item.Item;
import com.fitivation_v3.config.ObjectIdSerializer;
import com.fitivation_v3.package_facility.PackageFacility;
import com.fitivation_v3.security.service.UserDetailsImpl;
import com.fitivation_v3.user.User;
import java.time.OffsetDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "bills")
public class Bill {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId id;

  private String customerIdStripe;
  private ObjectId ownerId;
  private String paymentIntent;
  private Item item;
  private double totalPrice;
  private boolean status = false;

  @CreatedDate
  private Date dateCreated;

  @LastModifiedDate
  private Date lastUpdated;

  public double caculateTotalPrice() {
    double sum = item.getQuantity() * (1 - item.getPackageFacility().getDiscount())
        * item.getPackageFacility().getBasePrice()
        * item.getPackageFacility().getType();
    setTotalPrice(sum);

    return totalPrice;
  }

  public static Bill createBillFromCart(Cart cart, String paymentIntent, User user) {
    System.out.println(
        "ownerId " + cart.getItems().get(0).getPackageFacility().getFacility().getOwnerId());
    Bill bill = Bill.builder().customerIdStripe(user.getCustomerIdStripe())
        .paymentIntent(paymentIntent).item(cart.getItems().get(0)).totalPrice(cart.getTotalPrice())
        .ownerId(cart.getItems().get(0).getPackageFacility().getFacility().getOwnerId())
        .customerIdStripe(user.getCustomerIdStripe())
        .status(false).build();

    return bill;
  }

}
