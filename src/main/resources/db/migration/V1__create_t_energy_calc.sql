create table if not exists t_energy_calc
(
    id                      bigserial primary key,
    c_date_created          timestamp not null,
    C_day_parameters        DECIMAL not null,
    c_night_parameters      DECIMAL not null,
    c_amount_day            DECIMAL not null,
    c_amount_night          DECIMAL not null,
    c_total_amount          DECIMAL not null
);

comment on column t_energy_calc.id is 'Первичный ключ';
comment on column t_energy_calc.c_date_created is 'Дата создания записи';
comment on column t_energy_calc.C_day_parameters is 'Показания день';
comment on column t_energy_calc.c_night_parameters is 'Показания ночь';
comment on column t_energy_calc.c_amount_day is 'Сумма за дневные показания';
comment on column t_energy_calc.c_amount_night is 'Сумма за ночные показания';
comment on column t_energy_calc.c_total_amount is 'Сумма всего к оплате';

alter table t_energy_calc
    owner to wind_energy;