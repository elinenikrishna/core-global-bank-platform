FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/api-gateway-1.0.0.jar app.jar
USER 10001
EXPOSE 8080
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]
