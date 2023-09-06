package com.fitivation_v3.stripe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

  private String number;
  private int exp_month;
  private int exp_year;
  private String cvc;
}
