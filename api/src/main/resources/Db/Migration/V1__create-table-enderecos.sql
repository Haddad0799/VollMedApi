
CREATE TABLE enderecos (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           logradouro VARCHAR(255) NOT NULL,
                           bairro VARCHAR(255) NOT NULL,
                           cep VARCHAR(10) NOT NULL,
                           cidade VARCHAR(255) NOT NULL,
                           uf ENUM('AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO') NOT NULL,
                           numero VARCHAR(10) NOT NULL,
                           complemento VARCHAR(255)
);
