package com.example.energycalculator.service.impl;

import com.example.energycalculator.persistance.entity.EnergyEntity;
import com.example.energycalculator.persistance.entity.TarifEntity;
import com.example.energycalculator.mapper.ECalcMapper;
import com.example.energycalculator.rest.model.EnergyCalcRequest;
import com.example.energycalculator.rest.model.EnergyCalcResponse;
import com.example.energycalculator.rest.model.EnergyTarifRequest;
import com.example.energycalculator.persistance.repository.EnergyRepo;
import com.example.energycalculator.persistance.repository.TarifRepo;
import com.example.energycalculator.service.EnergyCalcService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EnergyCalcServiceImpl implements EnergyCalcService {

  private final ECalcMapper eCalcMapper;

  private final EnergyRepo energyRepo;

  private final TarifRepo tarifRepo;

  private BigDecimal dayRequestParameters;

  private BigDecimal dayPreviousParameters;

  private BigDecimal nightRequestParameters;

  private BigDecimal nightPreviousParameters;

  public EnergyCalcServiceImpl(
      ECalcMapper eCalcMapper,
      EnergyRepo energyRepo,
      TarifRepo tarifRepo) {
    this.eCalcMapper = eCalcMapper;
    this.energyRepo = energyRepo;
    this.tarifRepo = tarifRepo;
  }

  @Override
  public EnergyCalcResponse energyCalculator(EnergyCalcRequest request) {
    log.debug("Start energyCalculator method");

    log.debug("Проверяем, что показание корректные");
    var errMessage = checkRequest(request);
    if (null != errMessage) {
      return eCalcMapper.badResponseWithErrorMsg(errMessage);
    }

    log.debug("Установим текущие показания для дальнейшей калькуляции");
    dayRequestParameters = new BigDecimal(request.dayParameters());
    nightRequestParameters = new BigDecimal(request.nightParameters());

    log.debug("Проверим существуют ли предыдущие записи");
    var previousRecord = energyRepo.findFirstByOrderByDateCreatedDesc();
    if (null != previousRecord) {

      log.debug("Установим показания предыдущей записи, для дальнейшей калькуляции");
      dayPreviousParameters = previousRecord.getDayParameters();
      nightPreviousParameters = previousRecord.getNightParameters();

      log.debug("Проверим текущие показания, сравним с показаниями предыдущей записи");
      var checkParameters = checkParametersRequest();
      if (null != checkParameters) {
        return eCalcMapper.badResponseWithErrorMsg(checkParameters);
      }

      log.debug("Stop energyCalculator method with previous data. Success.");
      return calculateEnergy();
    }

    log.debug("Stop energyCalculator method without previous data. Success.");
    return calculateEnergy();
  }

  @Override
  public String setEnergyTarif(EnergyTarifRequest request) {
    log.debug("Start setEnergyTarif method");


    var checkTarif = checkTarifRequest(request);
    if (null != checkTarif) {
      throw new IllegalArgumentException(checkTarif);
    }

    var tarifEntity = TarifEntity.builder()
        .dateCreated(LocalDateTime.now())
        .dayTarif(new BigDecimal(request.dayTarif()))
        .nightTarif(new BigDecimal(request.nightTarif()))
        .build();

    tarifRepo.save(tarifEntity);

    return "Новый тариф добавлен и будет использован для расчета";
  }

  // Как сформировать ссылку на оплату для сбп
  // Возможность интеграции с кометой

  /**
   * Калькулируем, создаем и сохраняем запись и возвращаем ответ
   *
   * @return ответ
   */
  private EnergyCalcResponse calculateEnergy() {
    log.debug("Start calculateEnergy method");

    // Инициализируем тариф
    var currentTarif = tarifRepo.findFirstByOrderByDateCreatedDesc();
    if (null == currentTarif) {
      return eCalcMapper.badResponseWithErrorMsg(
          "В настоящий момент отсутствует доступный тарифный план, по которому могли бы рассчитать показания");
    }
    log.debug("Установим тариф по которому будем калькулировать сумма оплаты");
    var dayTarif = currentTarif.getDayTarif();
    var nightTarif = currentTarif.getNightTarif();

    // Калькулируем
    log.debug("Производим расчет оплаты за день");
    var dayAmount = null != dayPreviousParameters
        ? dayTarif.multiply(dayRequestParameters.subtract(dayPreviousParameters))
        : dayRequestParameters.multiply(dayTarif);

    log.debug("Производим расчет оплаты за ночь");
    var nightAmount = null != nightPreviousParameters
        ? nightTarif.multiply(nightRequestParameters.subtract(nightPreviousParameters))
        : nightRequestParameters.multiply(nightTarif);

    // Создаем сущность
    log.debug("Создаем запись с последними показаниями");
    var entityNewRecord = getNewRecord(dayAmount, nightAmount);

    // Сохраняем сущность
    log.debug("Сохраняем запись с последними показаниями");
    var responseEntity = energyRepo.save(entityNewRecord);

    log.debug("Stop calculateEnergy method");
    return EnergyCalcResponse.builder()
        .amountDay(String.valueOf(responseEntity.getAmountDay()))
        .amountNight(String.valueOf(responseEntity.getAmountNight()))
        .totalAmount(String.valueOf(responseEntity.getTotalAmount()))
        .message("Успешна внесена новая запись!")
        .build();

  }

  /**
   * Проверяем был ли отправлен параметр тарифа, т.к. если не отправлять, будет использован тариф с
   * предыдущей записи
   *
   * @param request
   * @return
   */
  private String checkTarifRequest(EnergyTarifRequest request) {

    if (null == request.dayTarif()) {
      return "dayTarif is null";
    }

    if (null == request.nightTarif()) {
      return "nightTarif is null";
    }

    try {
      new BigDecimal(request.dayTarif());
    } catch (NumberFormatException e) {
      return "Числовой формат dayTarif не верный, проверьте корректность ввода";
    }

    try {
      new BigDecimal(request.nightTarif());
    } catch (NumberFormatException e) {
      return "Числовой формат nightTarif не верный, проверьте корректность ввода";
    }

    if (new BigDecimal(request.dayTarif()).compareTo(BigDecimal.ZERO) <= 0) {
      return "dayTarif Is not a positive value";
    }

    if (new BigDecimal(request.nightTarif()).compareTo(BigDecimal.ZERO) <= 0) {
      return "nightParameters Is not a positive value";
    }

    return null;

  }

  /**
   * Создаем сущность
   *
   * @param newRecordDayAmount
   * @param newRecordNightAmount
   * @return
   */
  private EnergyEntity getNewRecord(BigDecimal newRecordDayAmount,
      BigDecimal newRecordNightAmount) {
    return EnergyEntity.builder()
        .dateCreated(LocalDateTime.now())
        .dayParameters(dayRequestParameters)
        .nightParameters(nightRequestParameters)
        .amountDay(newRecordDayAmount)
        .amountNight(newRecordNightAmount)
        .totalAmount(newRecordDayAmount.add(newRecordNightAmount))
        .build();
  }

  /**
   * Проверяем указан ли тариф
   *
   * @param request
   * @return
   */
  private String checkRequest(EnergyCalcRequest request) {

    if (null == request) {
      return "EnergyCalcRequest is null";
    }

    if (request.dayParameters().isEmpty()) {
      return "dayParameters is null";
    }

    if (request.nightParameters().isEmpty()) {
      return "nightParameters is null";
    }

    try {
      new BigDecimal(request.dayParameters());
    } catch (NumberFormatException e) {
      return "Числовой формат dayParameters не верный, проверьте корректность ввода";
    }

    try {
      new BigDecimal(request.nightParameters());
    } catch (NumberFormatException e) {
      return "Числовой формат nightParameters не верный, проверьте корректность ввода";
    }

    //if (Integer.parseInt(request.dayParameters()) <= 0) return "dayParameters Is not a positive value";
    if (new BigDecimal(request.dayParameters()).compareTo(BigDecimal.ZERO) <= 0) {
      return "dayParameters Is not a positive value";
    }

    if (new BigDecimal(request.nightParameters()).compareTo(BigDecimal.ZERO) <= 0) {
      return "nightParameters Is not a positive value";
    }

    return null;
  }

  private String checkParametersRequest() {

    if (dayRequestParameters.compareTo(dayPreviousParameters) <= 0) {
      return "Current day parameters is less than previous record";
    }

    if (nightRequestParameters.compareTo(nightPreviousParameters) <= 0) {
      return "Current night parameters is less than previous record";
    }

    return null;
  }
}