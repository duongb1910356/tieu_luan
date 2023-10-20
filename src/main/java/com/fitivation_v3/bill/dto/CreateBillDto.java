package com.fitivation_v3.bill.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fitivation_v3.cart_item.Item;
import com.fitivation_v3.config.ObjectIdSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBillDto {

  private ObjectId id;
  private String customerIdStripe;
  private String paymentIntent;
  private Item item;
  private double totalPrice;

}
