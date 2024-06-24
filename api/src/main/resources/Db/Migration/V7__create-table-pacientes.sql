CREATE TABLE pacientes (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nome VARCHAR(255) NOT NULL,
                           cpf VARCHAR(14) NOT NULL,
                           telefone VARCHAR(20),
                           endereco_logradouro VARCHAR(255) NOT NULL,
                           endereco_numero INT,
                           endereco_complemento VARCHAR(255),
                           endereco_bairro VARCHAR(255) NOT NULL,
                           endereco_cidade VARCHAR(255) NOT NULL,
                           endereco_uf VARCHAR(2) NOT NULL,
                           endereco_cep VARCHAR(8) NOT NULL
);

