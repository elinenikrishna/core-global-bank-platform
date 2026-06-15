FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/chatbot-service-1.0.0.jar app.jar
USER 10001
EXPOSE 8090
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]
