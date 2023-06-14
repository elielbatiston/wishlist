FROM maven:3.8.3-openjdk-17 as build

WORKDIR /home/app

COPY src src
COPY pom.xml .

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build /home/app/target/wishlist*.jar wishlist.jar

EXPOSE 8080

CMD ["java", "-jar", "wishlist.jar"]
