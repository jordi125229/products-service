FROM amazoncorretto:21-alpine
LABEL maintainer="Piotr"
COPY adapter/target/adapter-0.0.1-SNAPSHOT.jar product-service-app.jar
ENTRYPOINT ["java","-jar","/product-service-app.jar"]