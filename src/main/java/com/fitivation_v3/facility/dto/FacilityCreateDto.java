package com.fitivation_v3.facility.dto;

import com.fitivation_v3.address.Address;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import java.lang.reflect.Array;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Getter
@Setter
@AllArgsConstructor
public class FacilityCreateDto {

  private Address address;
  //  private ArrayList<Double> location;
  private GeoJsonPoint location;
  private String describe;
  private String name;
  private String phone;
  private String email;
}
