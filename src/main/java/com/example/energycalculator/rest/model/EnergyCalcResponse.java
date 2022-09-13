package com.example.energycalculator.rest.model;

import lombok.Builder;

public record EnergyCalcResponse(

    String amountDay,

    String amountNight,

    String totalAmount,

    String message
) {
  @Builder public EnergyCalcResponse{}
}
