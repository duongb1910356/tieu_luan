package com.fitivation_v3.cart;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fitivation_v3.cart_item.Item;
import com.fitivation_v3.config.ObjectIdSerializer;
import com.fitivation_v3.package_facility.PackageFacility;
import com.fitivation_v3.user.User;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "carts")
public class Cart {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId id;

  @DBRef
  private User user;

  @DBRef
  @Field("items")
  private List<Item> items = new ArrayList<>();

  @Transient
  private double totalPrice;

  @Transient
  private double originPrice;

  public double getTotalPrice() {
    double sum = 0;
    for (Item item : items) {
      PackageFacility packageItem = item.getPackageFacility();
      sum += item.getQuantity() * (1 - packageItem.getDiscount()) * packageItem.getBasePrice()
          * packageItem.getType();
    }
    totalPrice = sum;

    return totalPrice;
  }

  public double getOriginPrice() {
    double sum = 0;
    for (Item item : items) {
      PackageFacility packageItem = item.getPackageFacility();
      sum += item.getQuantity() * packageItem.getBasePrice() + packageItem.getType();
    }
    originPrice = sum;

    return originPrice;
  }

  public void addItemToCart(Item item) {
    items.add(item);
  }

  public void deleteItemFromCart(ObjectId itemId) {
    items.removeIf(item -> item.getId().equals(itemId));
  }
}
