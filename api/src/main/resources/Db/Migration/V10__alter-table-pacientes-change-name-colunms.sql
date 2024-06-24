ALTER TABLE pacientes
    CHANGE COLUMN endereco_logradouro logradouro VARCHAR(255),
    CHANGE COLUMN endereco_numero numero INT,
    CHANGE COLUMN endereco_complemento complemento VARCHAR(255),
    CHANGE COLUMN endereco_bairro bairro VARCHAR(255),
    CHANGE COLUMN endereco_cidade cidade VARCHAR(255),
    CHANGE COLUMN endereco_uf uf VARCHAR(2),
    CHANGE COLUMN endereco_cep cep VARCHAR(8);
