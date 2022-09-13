package com.example.energycalculator.mapper;

import com.example.energycalculator.rest.model.EnergyCalcResponse;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    imports = {LocalDateTime.class}
)
public interface ECalcMapper {

  @Mapping(target = "message", source = "msg")
  EnergyCalcResponse badResponseWithErrorMsg(String msg);

}
