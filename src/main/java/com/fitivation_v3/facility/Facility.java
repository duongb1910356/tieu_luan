package com.fitivation_v3.facility;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fitivation_v3.address.Address;
import com.fitivation_v3.config.ObjectIdSerializer;
import com.fitivation_v3.files.FileData;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "facilities")
public class Facility {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId id;

  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId ownerId;

  private Address address;
  private String slugAddress;

  @Indexed(name = "location_index", background = true)
  private GeoJsonPoint location;

//  @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
//  private Point location;

  private float avagerstar;
  private String describe;
  private String name;
  private String phone;
  private String email;
  private double distance;
  private List<FileData> images;
}
