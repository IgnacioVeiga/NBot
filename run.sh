#!/bin/bash

set -e

# Dar permisos de ejecución la primera vez con: chmod +x run.sh

# Ejecuta el wrapper de Maven para compilar (shade genera target/app.jar)
./mvnw clean package -DskipTests

# Ejecuta el jar ejecutable con dependencias
java -jar target/app.jar
