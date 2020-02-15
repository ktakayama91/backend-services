FROM openjdk:8-jdk-alpine
EXPOSE 8080
WORKDIR /opt
ADD target/backend-services-*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]