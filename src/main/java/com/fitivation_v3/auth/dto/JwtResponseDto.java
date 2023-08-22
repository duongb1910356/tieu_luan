package com.fitivation_v3.auth.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class JwtResponseDto {
  private String accessToken;
  private String type = "Bearer";
  private ObjectId id;
  private String username;
  private String email;
  private List<String> roles;
  private String refreshToken;

  public JwtResponseDto(String accessToken, String refreshToken, ObjectId id, String username, String email, List<String> roles) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
  }
}
