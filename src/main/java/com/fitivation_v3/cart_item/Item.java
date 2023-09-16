package com.fitivation_v3.cart_item;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fitivation_v3.config.ObjectIdSerializer;
import com.fitivation_v3.package_facility.PackageFacility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "items")
public class Item {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId id;

  @DBRef
  private PackageFacility packageFacility;

  private int quantity;

  public Item(PackageFacility packageFacility, int quantity) {
    this.packageFacility = packageFacility;
    this.quantity = quantity;
  }

  public String toString() {
    return "[" + this.packageFacility + ", " + this.quantity + "]";
  }
}
