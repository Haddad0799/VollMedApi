
CREATE TABLE medicos (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) UNIQUE NOT NULL,
                         telefone VARCHAR(20) UNIQUE NOT NULL,
                         crm BIGINT UNIQUE NOT NULL,
                         especialidade ENUM('ORTOPEDIA', 'CARDIOLOGIA', 'GINECOLOGIA', 'DERMATOLOGIA') NOT NULL,
                         endereco_id BIGINT,
                         ativo BOOLEAN NOT NULL,
                         FOREIGN KEY (endereco_id) REFERENCES enderecos(id)
);
