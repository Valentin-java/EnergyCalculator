create table if not exists t_tarif
(
    id                      bigserial primary key,
    c_date_created          timestamp not null,
    c_day_tarif             DECIMAL not null,
    c_night_tarif           DECIMAL not null
);

comment on column t_tarif.id is 'Первичный ключ';
comment on column t_tarif.c_date_created is 'Дата создания записи';
comment on column t_tarif.c_day_tarif is 'Дневной тариф';
comment on column t_tarif.c_night_tarif is 'Ночной тариф';

alter table t_tarif
    owner to wind_energy;