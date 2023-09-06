package com.fitivation_v3.stripe.model;

public class ChargeRequest {

  public enum Currency {
    EUR, USD;
  }

  private String description;
  private int amount;
  private Currency currency;
  private String stripeEmail;
  private String stripeToken;
}
