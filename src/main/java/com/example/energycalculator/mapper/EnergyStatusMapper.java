package com.example.energycalculator.mapper;

import com.example.energycalculator.persistance.entity.EnergyEntity;
import com.example.energycalculator.rest.model.EnergyStatusDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring"
)
public interface EnergyStatusMapper {

  @Mapping(target = "message", ignore = true)
  EnergyStatusDto toDto(EnergyEntity entity);

  @AfterMapping
  default void createMessage(@MappingTarget EnergyStatusDto target, EnergyEntity entity) {
    StringBuilder sb = new StringBuilder();
    sb.append("Текущие показания электроэнергии: ");
    sb.append("Показания за день: ");
    sb.append(entity.getDayParameters());
    sb.append(" ");
    sb.append("Показания за ночь: ");
    sb.append(entity.getNightParameters());

    target.setMessage(sb.toString());

  }
}
