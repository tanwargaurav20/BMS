FROM openjdk:8-jdk-alpine
MAINTAINER BMS
COPY target/BMS-*.jar BMS.jar
ENTRYPOINT ["java","-jar","/BMS.jar"]