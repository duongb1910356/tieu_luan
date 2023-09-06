package com.fitivation_v3.stripe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentItentDto {

  private long amount;
  private String currency;
  private long applicationFeeAmount;
}
