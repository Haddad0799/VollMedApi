
ALTER TABLE pacientes
    CHANGE COLUMN nome nome VARCHAR(255) AFTER id,
    CHANGE COLUMN cpf cpf VARCHAR(14) AFTER nome,
    CHANGE COLUMN telefone telefone VARCHAR(20) AFTER cpf,
    CHANGE COLUMN tipo_sanguineo tipo_sanguineo VARCHAR(10) AFTER telefone,
    CHANGE COLUMN data_nasc data_nasc DATE AFTER tipo_sanguineo,
    CHANGE COLUMN idade idade INT AFTER data_nasc,
    CHANGE COLUMN peso peso DOUBLE PRECISION AFTER idade,
    CHANGE COLUMN endereco_logradouro endereco_logradouro VARCHAR(255) AFTER peso,
    CHANGE COLUMN endereco_numero endereco_numero INT AFTER endereco_logradouro,
    CHANGE COLUMN endereco_complemento endereco_complemento VARCHAR(255) AFTER endereco_numero,
    CHANGE COLUMN endereco_bairro endereco_bairro VARCHAR(255) AFTER endereco_complemento,
    CHANGE COLUMN endereco_cidade endereco_cidade VARCHAR(255) AFTER endereco_bairro,
    CHANGE COLUMN endereco_uf endereco_uf VARCHAR(2) AFTER endereco_cidade,
    CHANGE COLUMN endereco_cep endereco_cep VARCHAR(8) AFTER endereco_uf;
