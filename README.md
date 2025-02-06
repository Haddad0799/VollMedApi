# 🏥 **Voll Med API - Api de Agendamento de Consultas Médicas.**
## ✏️ **Descrição**
### Esta API foi desenvolvida para fins de aprendizado pessoal, com o objetivo de testar minhas habilidades no desenvolvimento de funcionalidades que exigem validações de regras de negócio, seguindo boas práticas de desenvolvimento de software.

### Além disso, este projeto também foca na escrita de testes unitários para garantir que os métodos e funcionalidades mais relevantes da aplicação estão funcionando corretamente.

### Também foi meu primeiro contato com autenticação via tokens JWT, onde implementei um filtro para bloquear requisições que não possuem um token válido no cabeçalho `Authentication` das requisições HTTP para os endpoints que exigem autenticação.

# 🛠️ **Tecnologias utilizadas**
- **Linguagem:** Java 17
- **Framework:** Spring Boot
- **Banco de Dados:** MySQL
- **ORM:** Spring Data JPA
- **Migrations:** Flyway
- **Autenticação e Segurança:** JWT
- **Documentação:** Swagger
- **Testes:** JUnit + Mockito
- **Gerenciamento de Dependências:** Maven

## **Para Começar**
**Clone o Repositório:**  
   Execute o comando abaixo no terminal:  

   ```sh
   git clone https://github.com/Haddad0799/screenmatch.git
   ```  

## ⚙️ **Instalação e Configuração**  
### **Pré-requisitos**  
#### Antes de rodar o projeto, certifique-se de ter instalado:
- **Java 17** [Baixar JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **MySql:** [Baixar MySQL windows](https://dev.mysql.com/downloads/installer/) ; [Instalar MySQL no Linux](https://dev.mysql.com/doc/refman/8.0/en/linux-installation.html) ; [Baixar MySQL para Mac](https://dev.mysql.com/downloads/mysql/)

### **Criação do Banco de Dados**  
Após a instalação do MySQL, execute o seguinte comando no terminal ou no cliente SQL:  

```sql
CREATE DATABASE vollmed;
```
### **Configuração das Variáveis de Ambiente do banco de dados**
#### Crie as seguintes variaveis de ambiente:
- **MYSQL_DB_HOST:** Endereço do host do banco de dados.  
- **MYSQL_DB_USERNAME:** Usuário do banco de dados.  
- **MYSQL_DB_PASSWORD:** Senha do banco de dados.
#### **Adicione suas variáveis de ambiente no projeto**
- **Path:** `application.properties`
```properties
spring.datasource.url=jdbc:mysql://${MYSQL_DB_HOST}/vollmed
spring.datasource.username=${MYSQL_DB_USERNAME}
spring.datasource.password=${MYSQL_DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
hibernate.dialect=org.hibernate.dialect.MySQLDialect
```
## 🔒 **Autenticação e Segurança**
Este projeto implementa autenticação via **JWT (JSON Web Token)** para garantir a segurança dos endpoints protegidos.  

### 🔹 Como funciona a autenticação?  
- O usuário faz login enviando suas credenciais para o endpoint `/login`.  
- Se as credenciais forem válidas, um **token JWT** será gerado e retornado na resposta.  
- Esse token deve ser enviado no cabeçalho **Authorization** para acessar os endpoints protegidos.  

### 🔹 Filtro de autenticação  
- Um **filtro JWT** foi implementado para bloquear requisições que não possuem um token válido.  
- Esse filtro verifica a presença do token no cabeçalho **Authorization** e valida sua assinatura.  
- Caso o token seja inválido ou ausente, a requisição é negada.

### 🔹 JWT_SECRET
- Essa é a variável de ambiente que contém a assinatura do token, e garante a ientegridade e autenticidade das informações contidas no token.
- Crie essa variável de ambiente e adicione ao `application.properties`

```properties
jwt.secret=${JWT_SECRET}
````








   

