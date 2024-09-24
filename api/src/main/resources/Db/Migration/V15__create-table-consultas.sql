CREATE TABLE consultas (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           paciente_id BIGINT NOT NULL,
                           medico_id BIGINT NOT NULL,
                           data_consulta DATETIME NOT NULL,
                           FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
                           FOREIGN KEY (medico_id) REFERENCES medicos(id)
);
