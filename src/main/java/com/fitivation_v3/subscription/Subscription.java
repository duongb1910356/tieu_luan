package com.fitivation_v3.subscription;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fitivation_v3.config.ObjectIdSerializer;
import com.fitivation_v3.package_facility.PackageFacility;
import com.fitivation_v3.user.User;
import java.util.Calendar;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@Document(collection = "subscriptions")
public class Subscription {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId id;

  @DBRef
  private User user;

  @DBRef
  private PackageFacility packageFacility;

  private String timeRegister;

  private Date expireDay;

  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId facilityId;

  void caclutateExpireDay() {
    Calendar calendar = Calendar.getInstance();
    Date currentDate = calendar.getTime();

    calendar.add(Calendar.DATE, packageFacility.getType() * 31);

    setExpireDay(calendar.getTime());
  }

}
