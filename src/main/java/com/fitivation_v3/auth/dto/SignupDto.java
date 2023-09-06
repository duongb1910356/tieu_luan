package com.fitivation_v3.auth.dto;

import java.util.Set;
import lombok.Getter;

@Getter
public class SignupDto {

  private String username;
  private String email;
  private Set<String> role;
  private String password;

  private String phone;
  private String displayName;

}
