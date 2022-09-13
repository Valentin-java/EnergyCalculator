package com.example.energycalculator.rest.model;

import lombok.Builder;

public record EnergyTarifRequest(

    String dayTarif,

    String nightTarif
) {
  @Builder public EnergyTarifRequest{}
}
