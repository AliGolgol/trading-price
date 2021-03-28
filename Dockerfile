FROM maven:3.3-jdk-8 AS build

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

FROM openjdk:8-jdk-alpine
COPY --from=build /usr/src/app/target/trading-price.jar /usr/app/trading-price.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/trading-price.jar"]