FROM openjdk:21-jdk-slim
LABEL authors="LibertyD"
WORKDIR /app
COPY target/airbnb-backend-0.0.1-SNAPSHOT.jar app.jar
ENV APP_PORT=8080
EXPOSE $APP_PORT
ENTRYPOINT ["java", "-jar", "app.jar"]
