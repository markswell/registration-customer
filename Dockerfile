FROM openjdk:17-oracle

ARG JAR_FILE=target/customer.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]