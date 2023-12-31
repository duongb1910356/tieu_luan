package com.fitivation_v3.package_facility;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fitivation_v3.config.ObjectIdSerializer;
import com.fitivation_v3.facility.Facility;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "packagefacilities")
public class PackageFacility {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId id;
  private String name;
  private int basePrice;
  private int type;
  private float discount;

  @DBRef
  private Facility facility;
}
