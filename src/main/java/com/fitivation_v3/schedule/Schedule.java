package com.fitivation_v3.schedule;

import com.fitivation_v3.facility.Facility;
import java.util.Set;
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
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "schedules")
public class Schedule {

  @Id
  private ObjectId id;

  @DBRef
  private Facility facility;

  private Set<OpenTime> openTimes;
}
