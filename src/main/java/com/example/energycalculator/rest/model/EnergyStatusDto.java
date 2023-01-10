package com.example.energycalculator.rest.model;

import lombok.Data;

@Data
public class EnergyStatusDto {
  private String amountDay;

  private String amountNight;

  private String totalAmount;

  private String message;
}
