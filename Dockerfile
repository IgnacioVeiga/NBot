# Imagen base con Java y Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

ARG BOT_TOKEN=BOT_TOKEN
ARG WEATHER_API_KEY=WEATHER_API_KEY

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos de proyecto
COPY pom.xml .
COPY src ./src

# Descarga dependencias y compila el proyecto
RUN mvn clean package -DskipTests

# Imagen final
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia el JAR generado (fat jar generado por maven-shade-plugin)
COPY --from=build /app/target/NBot-1.0-SNAPSHOT-shaded.jar /app/NBot.jar

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/app/NBot.jar"]
