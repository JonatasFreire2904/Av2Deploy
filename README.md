# 🔐 API de Autenticação e Autorização JWT - AV2

Este projeto faz parte da avaliação AV2 da disciplina, com foco em segurança, autenticação, autorização, testes, monitoramento e documentação de APIs com Spring Boot 3.x.

> 🧠 **Observação**: Esta API foi construída com base em um projeto anterior já funcional, com ajustes e aprimoramentos para cumprir todos os requisitos desta atividade. Essa abordagem permitiu agilidade no desenvolvimento e entrega do trabalho.

---

## ✅ Tecnologias Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Security
- OAuth2 Resource Server (JWT)
- Spring Data JPA
- H2 Database
- Springdoc OpenAPI (Swagger)
- Spring Boot Actuator
- Prometheus
- JUnit 5 + Mockito
- JMeter
- Docker (configurado, mas **deploy foi desconsiderado**)

---

## 📦 Dependências (principais)

```xml
<!-- pom.xml contém -->
spring-boot-starter-web  
spring-boot-starter-security  
spring-boot-starter-oauth2-resource-server  
spring-boot-starter-data-jpa  
com.h2database:h2  
org.springdoc:springdoc-openapi-starter-webmvc-ui  
spring-boot-devtools  
lombok  
spring-boot-starter-test  
spring-boot-starter-actuator  
micrometer-registry-prometheus
```

---

## ⚙️ Configuração do Ambiente

Arquivo `application.yml` com:

- Configuração do banco H2
- Chave secreta JWT
- Configuração do Swagger
- Exposição de métricas com Actuator e Prometheus

Acesse:
- H2: `http://localhost:8080/h2-console`
- Swagger: `http://localhost:8080/swagger-ui.html`
- Health check: `http://localhost:8080/actuator/health`
- Prometheus: `http://localhost:8080/actuator/prometheus`

---

## 🛡️ Endpoints de Autenticação

- `POST /auth/login` → Login e geração de token JWT  
- `POST /auth/validate` → Validação de token JWT  
- `GET /api/hello` → Endpoint protegido para qualquer usuário autenticado  
- `GET /api/admin` → Endpoint protegido exclusivo para administradores

---

## 🔐 Segurança

- Criptografia de senhas com BCrypt
- JWT assinado com HMAC SHA-256
- Controle de acesso com `hasRole` via Spring Security
- Autenticação stateless com Resource Server

---

## ✅ Testes Automatizados

Testes com JUnit (e Mockito) cobrindo:

- Autenticação correta e incorreta
- Acesso com e sem token
- Validação de roles (`USER` e `ADMIN`)

Arquivos localizados em `src/test/java`.

---

## 📈 Testes de Carga (JMeter)

Arquivo `.jmx` incluso com plano de teste que simula:

- 200 usuários simultâneos
- 10 requisições de login por usuário
- Métricas como **Throughput**, **Error %** e **Average Response Time**

---

## 📚 Documentação Swagger

Gerada automaticamente com Springdoc OpenAPI.

Acesse em:
```
http://localhost:8080/swagger-ui.html
```

---

## 🩺 Monitoramento

- **Actuator** habilitado com endpoints:
  - `/actuator/health`
  - `/actuator/metrics`
  - `/actuator/prometheus`
- **Prometheus** configurado para coleta de métricas
- (Opcional) Integração com Grafana para dashboards

---

## 🚚 Deploy

O projeto está configurado para deploy com Docker:

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/AutenticacoJWT-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

> 🚫 O deploy **não foi enviado**, mas o container foi testado localmente com sucesso.

---

## 🧾 Como Rodar Localmente

1. Clone o repositório:
```bash
git clone https://github.com/seuusuario/AutenticacoJWT.git
```

2. Navegue até o diretório:
```bash
cd AutenticacoJWT
```

3. Rode com Maven:
```bash
mvn spring-boot:run
```

4. (Opcional) Construa com Docker:
```bash
mvn clean package -DskipTests
docker build -t jwt-api .
docker run -p 8080:8080 jwt-api
```

---

## 👨‍🏫 Considerações Finais

- Projeto funcional com autenticação segura via JWT.
- Testes automatizados garantem integridade da aplicação.
- Testes de carga realizados com JMeter.
- Monitoramento habilitado com Prometheus e Spring Actuator.

🎓 Desenvolvido para fins educacionais como parte da **AV2** do curso.

---
