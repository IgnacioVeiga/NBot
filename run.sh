#!/bin/bash

# Dar permisos de ejecución la primera vez con: chmod +x run.sh

# Ejecuta el wrapper de Maven para compilar y correr la app
./mvnw clean install

# Si la compilación fue exitosa, ejecutá la app
if [ $? -eq 0 ]; then
    java -jar target/NBot-1.0-SNAPSHOT.jar
else
    echo "La compilación falló."
fi