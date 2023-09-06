package com.fitivation_v3.stripe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonStripeDto {

  private String lastName;
  private String firstName;
  private String city;
  private String line1;
  private String line2;
  private String postalCode;
}
