FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY bitnagil-api/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
