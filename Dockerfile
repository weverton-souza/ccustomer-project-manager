# Estágio 1: Construção
FROM gradle:8.2.1-jdk21-alpine AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia apenas os arquivos de configuração do Gradle para cachear as dependências primeiro
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Baixa as dependências do projeto
RUN ./gradlew build --no-daemon || return 0

# Copia o código fonte do projeto
COPY src ./src

# Compila o projeto e gera o JAR
RUN ./gradlew clean build -x test --no-daemon

# Estágio 2: Execução
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR do estágio de construção
COPY --from=build /app/build/libs/customer-project-manager.jar ./customer-project-manager.jar

# Comando para executar o JAR
CMD ["java", "-jar", "customer-project-manager.jar"]
