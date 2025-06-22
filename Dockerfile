# Requiere variables de entorno, usar archivo .env o setearlas donde corresponda.
# Para hacer una build de la imágen: docker build -t nbot .
# Para crear el contenedor: docker run --env-file .env -p 8080:8080 nbot

# Etapa 1: Build con Maven
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen final con JRE Alpine (más liviana)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/app.jar app.jar
EXPOSE 8080

# Comando de inicio del contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]