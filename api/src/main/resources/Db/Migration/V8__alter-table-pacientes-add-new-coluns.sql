
ALTER TABLE pacientes
    ADD COLUMN tipo_sanguineo varchar(10),
ADD COLUMN data_nasc date,
ADD COLUMN idade int,
ADD COLUMN peso double precision;
