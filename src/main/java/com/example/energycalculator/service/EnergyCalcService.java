package com.example.energycalculator.service;

import com.example.energycalculator.rest.model.EnergyCalcRequest;
import com.example.energycalculator.rest.model.EnergyCalcResponse;
import com.example.energycalculator.rest.model.EnergyStatusDto;
import com.example.energycalculator.rest.model.EnergyTarifRequest;

public interface EnergyCalcService {

  EnergyCalcResponse energyCalculator(EnergyCalcRequest request);

  EnergyStatusDto getEnergyStatus();

  String setEnergyTarif(EnergyTarifRequest request);



}
