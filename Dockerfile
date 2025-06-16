# docker run -e BOT_TOKEN=telegram_token -e WEATHER_API_KEY=openwheather_api_key nbot

# Imagen base con Java y Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

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