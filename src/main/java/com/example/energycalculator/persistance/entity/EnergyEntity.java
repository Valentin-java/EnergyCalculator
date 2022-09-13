package com.example.energycalculator.persistance.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "t_energy_calc")
public class EnergyEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "c_date_created")
  private LocalDateTime dateCreated;

  @Column(name = "C_day_parameters")
  private BigDecimal dayParameters;

  @Column(name = "c_night_parameters")
  private BigDecimal nightParameters;

  @Column(name = "c_amount_day")
  private BigDecimal amountDay;

  @Column(name = "c_amount_night")
  private BigDecimal amountNight;

  @Column(name = "c_total_amount")
  private BigDecimal totalAmount;
}
