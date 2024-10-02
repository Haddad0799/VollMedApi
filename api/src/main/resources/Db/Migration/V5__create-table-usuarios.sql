CREATE TABLE usuarios (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          login VARCHAR(255) UNIQUE NOT NULL,
                          senha VARCHAR(255) NOT NULL
);
