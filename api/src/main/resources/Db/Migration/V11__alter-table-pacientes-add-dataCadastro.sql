ALTER TABLE pacientes
    ADD COLUMN data_cadastro DATE DEFAULT NULL;

UPDATE pacientes
SET data_cadastro = CURDATE()
WHERE id = 1;
