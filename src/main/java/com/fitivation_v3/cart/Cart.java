package com.fitivation_v3.cart;

import com.fitivation_v3.cart_item.Item;
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
  private ObjectId id;

  @DBRef
  private User user;

  @DBRef
  @Field("items")
  private List<Item> items = new ArrayList<>();

  @Transient
  private double totalPrice;

  public double getTotalPrice() {
    double total = 0;
    for (Item item : items) {
      PackageFacility packageItem = item.getPackageFacility();
      total += item.getQuantity() * (1 - packageItem.getDiscount()) * packageItem.getBasePrice();
    }
    totalPrice = total;

    return totalPrice;
  }

  public void addItemToCart(Item item) {
    items.add(item);
  }

  public void deleteItemFromCart(ObjectId itemId) {
    items.removeIf(item -> item.getId().equals(itemId));
  }
}
