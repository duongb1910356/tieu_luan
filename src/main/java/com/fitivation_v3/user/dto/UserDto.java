package com.fitivation_v3.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fitivation_v3.config.ObjectIdSerializer;
import com.fitivation_v3.user.Role;
import com.fitivation_v3.user.Sex;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId id;

  private String username; //email
  private Set<Role> roles;

  private String displayName;
  private String avatar;
  private Date birth;
  private String phone;
  private Sex sex;
}
