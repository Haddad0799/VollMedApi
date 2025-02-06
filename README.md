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
- Compreender a implementação do `securityFilterChain` e a ordem de chamada dos filtros, gerenciados pelo Spring. Isso me causou problemas, principalmente no tratamento de algumas exceções personalizadas que eram lançadas dentro do filtro de autenticação, exceções essas que não eram tratadas pelo meu `RestControllerAdcive`, mesmo estando configuradas para serem tratadas.
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
- Após compreender que algumas exceções como por exemplo as exceções lançadas no método getSubject(String token), não estavam sendo tratadas pelo meu `RestControllerAdvice`, por causa da ordem de execução dos filtros, e qualquer exceção lançada dentro do metodo `doFilterInternal` seria tratada por padrão pela classe do spring `ExceptionTranslationFilter`, que devolvia por padrão status 403 forbidden. Resolvi tratar com try-catch essas exceções dentro do método onde elas poderiam ocorrer.
```java
@SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {


        String requestUri = request.getRequestURI();

        // Ignorar as URLs que foram liberadas
        if (requestUri.equals("/login") ||
                requestUri.equals("/usuarios/cadastro") ||
                requestUri.startsWith("/swagger-ui") ||
                requestUri.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String tokenJwt = recuperarToken(request);
            var subject = tokenService.getSubject(tokenJwt);
            Optional<UserDetails> userDetails = usuarioRepository.findByLogin(subject);

            if (userDetails.isPresent()) {
                var usuario = userDetails.get();
                var usuarioAutenticado = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usuarioAutenticado);
            }  
        } catch (TokenNotProvidedException ex) { 
            // Resposta para token não fornecido
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(String.format("{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"Bad Request\",\"message\":\"%s\",\"path\":\"%s\"}",
                    LocalDateTime.now(), HttpServletResponse.SC_BAD_REQUEST, "Token não enviado no cabeçalho!", request.getRequestURI()));
            return;
        } catch (InvalidTokenException ex) {
            // Resposta para token inválido
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(String.format("{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"Unauthorized\",\"message\":\"%s\",\"path\":\"%s\"}",
                    LocalDateTime.now(), HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage(), request.getRequestURI()));
            return;
        }
        // Chamando os próximos filtros caso não haja erro.
        filterChain.doFilter(request, response);
    }
````
Embora não fosse a melhor maneira de resolver, foi a maneira que encontrei de tratar esses erros, e devolver respostas mais claras aos clientes dessa API.(Futuramente em outro projeto me deparei com o mesmo problema e resolvi desenvolvendo um `AuthenticationEntryPoint` e um `AccessDeniedHandler`)

###### Explicação
- O método recuperarToken() e getSubject() poderiam lançar TokenNotProvidedException e InvalidTokenException respectivamente, que eram exceções tratadas pelo RestControllerAdvice, mas como a exceção era bloqueada caso a autenticação falhasse, se ocorreu uma dessas exceptions é por que não houve autenticação, se não houve autenticação, a requisição não chega ao controller e o erro não é tratado devidamente.
 
 #### Validações com boas práticas
 
 ##### Para desenvolver a funcionalidade de agendamento de consultas, algumas validações eram necessárias, como por exemplo:
- ✅ Validar se a consulta respeita os dias e horários de funcionamento da clínica.
- ✅ Validar agendamentos com antecedência.
- ✅ Validar conflito de horários entre médicos e pacientes.
- ✅ Validar consultas duplicadas.

##### Para realizar essas validações, foi utilizado o polimorfismo da programação orientada a objetos, permitindo que todas as validações fossem feitas de maneira modular. Assim, a adição de novas validações ou modificação das existentes não interfere diretamente no método de agendamento de consultas. Para isso, foi utilizado um design pattern chamado Strategy.

##### **Implementação do Strategy Pattern**

- Inicialmente, foi criada uma interface com um método `validar`:
- 
````java
package net.val.api.consulta.service.agendarConsulta.validacoesDeAgendamento;

import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

@Component
public interface ValidarAgendamentoConsulta {

    void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta);
}

````

- Para cada classe que representa uma validação, essa interface é implementada. Dessa forma, aplicamos o conceito de polimorfismo: uma classe pode ser uma validação de conflito de horário, mas também uma validação de consulta.

 ````java
@Component
public class ValidarConflitoDeHorario implements ValidarAgendamentoConsulta {

    private final ConsultaRepository consultaRepository;

    public ValidarConflitoDeHorario(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Override
    public void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        LocalDateTime inicioConsulta = dadosAgendamentoConsulta.dataConsulta();
        LocalDateTime fimConsulta = inicioConsulta.plusMinutes(59);

        // Verifica se há alguma consulta do médico no intervalo de uma hora antes ou uma hora depois
        LocalDateTime intervaloInicio = inicioConsulta.minusMinutes(59);

        // Verificar se há conflito de horário para o médico
        if (consultaRepository.existsByMedicoIdAndDataConsultaBetween(dadosAgendamentoConsulta.medicoId(), intervaloInicio, fimConsulta)) {
            throw new ConflitoDeHorarioMedicoException(dadosAgendamentoConsulta.dataConsulta(), dadosAgendamentoConsulta.medicoId());
        }

        if (consultaRepository.existsByPacienteIdAndDataConsultaBetween(dadosAgendamentoConsulta.pacienteId(), intervaloInicio, fimConsulta)) {
            throw new ConflitoDeHorarioPacienteException(dadosAgendamentoConsulta.dataConsulta());
        }
    }
}
````
- Dentro do serviço de agendamento de consultas, utilizamos injeção de dependência para instanciar uma lista de validações.
 
````java
@Service
public class AgendarConsultaService {
 private final List<ValidarAgendamentoConsulta> validacoesAgendamentoConsulta;
}
````

- Agora, dentro do método de realização de consultas, chamamos todos os métodos `validar` das classes presentes na lista de validações. Cada uma delas possui sua própria lógica para validar a consulta, garantindo que todas as regras de negócio sejam respeitadas antes de efetivar o agendamento.

````java
@Service
public class AgendarConsultaService {
 private final List<ValidarAgendamentoConsulta> validacoesAgendamentoConsulta;

@Transactional
    public Consulta agendarConsulta(DadosAgendamentoConsulta agendamentoConsulta) {
       //Validações de consulta.
        validacoesAgendamentoConsulta.forEach(v -> v.validar(agendamentoConsulta));
   }
}
````
##### **Benefícios dessa abordagem**
- ✔ Baixo acoplamento → O serviço de agendamento não depende diretamente das regras de validação.
- ✔ Facilidade de manutenção → Novas validações podem ser adicionadas sem modificar o serviço principal.
- ✔ Extensibilidade → Se novas regras de negócio surgirem, basta criar uma nova classe de validação que implemente a interface, e ela será automaticamente incluída.
- ✔ Respeito ao Princípio Aberto/Fechado (OCP - Open/Closed Principle) → O código está preparado para extensão sem necessidade de modificações estruturais.



   

