package com.example.energycalculator.rest.model;

import lombok.Builder;

public record EnergyCalcRequest(

    String dayParameters,

    String nightParameters

) {
  @Builder public EnergyCalcRequest{}
}
