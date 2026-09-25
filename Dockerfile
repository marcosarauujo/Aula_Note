FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia o .jar que você gerou localmente no IntelliJ para dentro do Docker
COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]