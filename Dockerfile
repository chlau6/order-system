FROM gradle:7.6-jdk17-alpine AS builder

RUN rm -rf ./build
RUN mkdir -p /app
COPY . /app
WORKDIR /app
RUN gradle clean build


FROM openjdk:17-jdk-slim

RUN mkdir -p /app
COPY --from=builder /app/build/libs/order-system-demo.jar /app/app.jar
ENTRYPOINT ["java", "-jar","/app/app.jar"]
