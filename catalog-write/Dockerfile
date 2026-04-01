# Stage 1: Build - Usiamo Maven per compilare il codice sorgente
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
# Copiamo il pom.xml e scarichiamo le dipendenze (ottimizzazione cache)
COPY pom.xml .
RUN mvn dependency:go-offline
# Copiamo i sorgenti e creiamo il pacchetto JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run - Usiamo un JRE leggero per far girare l'applicazione
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copiamo solo il file compilato dallo stage precedente
COPY --from=build /app/target/*.jar app.jar
# Porta esposta (coerente con le slide e il compose)
EXPOSE 8081
# Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]