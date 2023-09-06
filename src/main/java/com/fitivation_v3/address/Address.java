package com.fitivation_v3.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

  private String province;
  private String district;
  private String ward;
  private String street;

  @Override
  public String toString() {
    return street + ", " + ward + " " + district + " " + province;
  }
}
