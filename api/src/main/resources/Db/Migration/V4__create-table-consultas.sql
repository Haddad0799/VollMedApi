CREATE TABLE consultas (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           paciente_id BIGINT NOT NULL,
                           medico_id BIGINT NOT NULL,
                           data_consulta DATETIME NOT NULL,
                           especialidade_medica ENUM('ORTOPEDIA', 'CARDIOLOGIA', 'GINECOLOGIA', 'DERMATOLOGIA') NOT NULL,
                           status ENUM('AGENDADA', 'CANCELADA', 'REALIZADA') NOT NULL,
                           CONSTRAINT fk_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
                           CONSTRAINT fk_medico FOREIGN KEY (medico_id) REFERENCES medicos(id)
);
