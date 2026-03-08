FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
