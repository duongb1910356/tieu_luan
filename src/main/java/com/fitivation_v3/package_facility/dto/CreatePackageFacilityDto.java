package com.fitivation_v3.package_facility.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePackageFacilityDto {

  private String name;
  private int basePrice;
  private Map<Integer, Float> discount;
  private ObjectId facilityId;
}
