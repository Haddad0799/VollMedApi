
CREATE TABLE pacientes (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nome VARCHAR(255) NOT NULL UNIQUE,
                           cpf VARCHAR(14) NOT NULL UNIQUE,
                           telefone VARCHAR(20) NOT NULL UNIQUE,
                           endereco_id BIGINT,
                           tipo_sanguineo ENUM('A_POSITIVO', 'A_NEGATIVO', 'B_POSITIVO', 'B_NEGATIVO', 'O_POSITIVO', 'O_NEGATIVO', 'AB_POSITIVO', 'AB_NEGATIVO') NOT NULL,
                           data_nasc DATE NOT NULL,
                           data_cadastro DATE NOT NULL,
                           idade INT NOT NULL,
                           peso DOUBLE NOT NULL,
                           ativo BOOLEAN NOT NULL,
                           FOREIGN KEY (endereco_id) REFERENCES enderecos(id)
);
