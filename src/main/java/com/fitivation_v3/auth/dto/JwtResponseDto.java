package com.fitivation_v3.auth.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fitivation_v3.config.ObjectIdSerializer;
import com.fitivation_v3.user.Sex;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class JwtResponseDto {

  private String accessToken;
  private String type = "Bearer";

  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId id;

  private String username;
  private List<String> roles;
  private String refreshToken;
  private String displayName;
  private String avatar;
  private Date birth;
  private String phone;
  private Sex sex;

  public JwtResponseDto(String accessToken, String refreshToken, ObjectId id, String username,
      List<String> roles, String displayName, String avatar, Date birth, String phone, Sex sex) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.id = id;
    this.username = username;
    this.roles = roles;
    this.displayName = displayName;
    this.avatar = avatar;
    this.birth = birth;
    this.phone = phone;
    this.sex = sex;
  }
}
