FROM maven:3.6-jdk-8-slim
WORKDIR /tests
COPY src /tests/src
COPY pom.xml /tests
WORKDIR /tests