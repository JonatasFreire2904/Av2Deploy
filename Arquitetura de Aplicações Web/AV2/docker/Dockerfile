# Usar Java 21 como base
FROM eclipse-temurin:21-jdk-alpine

# Diretório da aplicação
WORKDIR /app

# Copiar o JAR correto
COPY AutenticacoJWT-0.0.1-SNAPSHOT.jar app.jar

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]