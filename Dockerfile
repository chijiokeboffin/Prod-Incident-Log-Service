FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} incident-report.jar
ENTRYPOINT ["java", "-jar", "incident-report.jar"]