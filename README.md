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

## 🚀 **Para Começar**
**Clone o Repositório:**  
   Execute o comando abaixo no terminal:  

   ```sh
     https://github.com/Haddad0799/VollMedApi.git
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
### **Configuração das Variáveis de Ambiente**
#### Crie as seguintes variaveis de ambiente:
- **MYSQL_DB_HOST:** Endereço do host do banco de dados.  
- **MYSQL_DB_USERNAME:** Usuário do banco de dados.  
- **MYSQL_DB_PASSWORD:** Senha do banco de dados.
- **JWT_SECRET:** Essa é a variável de ambiente que contém a assinatura do token, e garante a ientegridade e autenticidade das informações contidas no token.
#### **Adicione suas variáveis de ambiente no projeto**
- **Path:** `application.properties`
##### **Variáveis do banco de dados**
```properties
spring.datasource.url=jdbc:mysql://${MYSQL_DB_HOST}/vollmed
spring.datasource.username=${MYSQL_DB_USERNAME}
spring.datasource.password=${MYSQL_DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
hibernate.dialect=org.hibernate.dialect.MySQLDialect
```
##### **JWT_SECRET**
```properties
jwt.secret=${JWT_SECRET}
````
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

## **Compilar e Executar a Aplicação**  

Execute o seguinte comando no terminal dentro do diretório do projeto:

```sh
mvn spring-boot:run
```

A aplicação estará rodando em: [http://localhost:8080](http://localhost:8080)

## 🌐 **Endpoints Disponíveis**  
Acesse a documentação Swagger da API para explorar os endpoints disponíveis:  

📌 **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 🚧 Desafios e Soluções

### Desafios
- Entender como funciona a autenticação via token JWT para implementá-la na minha API.
- Compreender a implementação do securityFilterChain e a ordem de chamada dos filtros, gerenciados pelo Spring. Isso me causou problemas, principalmente no tratamento de exceções personalizadas, pois, para endpoints bloqueados, mesmo com um token válido no momento da requisição, qualquer outro erro gerava uma resposta 401 Unauthorized.
- Implementar validações de maneira que seguisse boas práticas, sem que a classe service da funcionalidade de agendamento de consultas ficasse diretamente acoplada às validações. Caso contrário, sempre que uma nova validação fosse adicionada, removida ou modificada, seria necessário alterar o método responsável por validar, aprovar ou reprovar o agendamento, ferindo o princípio Open/Closed do SOLID, que afirma que uma classe deve estar aberta para expansão e fechada para modificação.

### Soluções
#### JWT
- Após entender que o JWT é dividido em 3 partes — o header, que carrega o tipo do token e o algoritmo de assinatura; o payload, que contém as informações do usuário autenticado; e a signature, que contém o segredo (secret) que garante a autenticidade do token —, foram implementados, com o auxílio da API JWT, um método para gerar esse token, inserindo essas informações, e outro para extrair essas informações e validá-las.

##### Método para gerar um token com header.payload.signature
```java
  public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(jwtSecret);
            Instant tempoDeExpiracao = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            return JWT.create()
                    .withIssuer("API Voll.Med")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(tempoDeExpiracao)
                    .sign(algoritmo);
        } catch (JWTCreationException ex) {
            throw new FalhaAoGerarTokenException();
        }
    }
````
###### Explicação
- gerarToken(Usuario usuario): Este método cria o token JWT com os dados do usuário, como o login (no payload) e a data de expiração (no payload). Ele utiliza o segredo (jwtSecret) para assinar o token com o algoritmo HMAC256.

##### Método para verificar autenticiade do token e extrair os dados de um token enviado
```java
public String getSubject(String token)  {

        try {
            var algoritmo = Algorithm.HMAC256(jwtSecret);

            return JWT.require(algoritmo)
                    .withIssuer("API Voll.Med")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTDecodeException  | TokenExpiredException ex) {
            throw new InvalidTokenException();
        }
    }
````
###### explicação
- getSubject(String token): Este método recebe um token, verifica sua autenticidade utilizando o segredo (jwtSecret) e, se o token for válido, extrai e retorna o subject (informações do usuário) do token. Caso o token seja inválido ou expirado, uma exceção é lançada.

#### SecurityFilterChain
-




   

