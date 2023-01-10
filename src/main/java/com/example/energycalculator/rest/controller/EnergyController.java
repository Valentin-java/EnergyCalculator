package com.example.energycalculator.rest.controller;

import com.example.energycalculator.rest.model.EnergyCalcRequest;
import com.example.energycalculator.rest.model.EnergyCalcResponse;
import com.example.energycalculator.rest.model.EnergyStatusDto;
import com.example.energycalculator.rest.model.EnergyTarifRequest;
import com.example.energycalculator.service.EnergyCalcService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/v1")
@RestController
public class EnergyController {

  private final EnergyCalcService energyCalcService;

  public EnergyController(
      EnergyCalcService energyCalcService) {
    this.energyCalcService = energyCalcService;
  }


  @PostMapping(
      path = "/energy-calc",
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE}
  )
  public ResponseEntity<EnergyCalcResponse> energyCalculator(@RequestBody EnergyCalcRequest request) {
    try {
      return new ResponseEntity<>(energyCalcService.energyCalculator(request), HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(
      path = "/set-tarif",
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE}
  )
  public ResponseEntity<String> setEnergyTarif(@RequestBody EnergyTarifRequest request) {
    try {
      return new ResponseEntity<>(energyCalcService.setEnergyTarif(request), HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(
      path = "/get-status",
      produces = {MediaType.APPLICATION_JSON_VALUE}
  )
  public ResponseEntity<EnergyStatusDto> getEnergyStatus() {
    try {
      return new ResponseEntity<>(energyCalcService.getEnergyStatus(), HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

}
