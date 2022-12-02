FROM openjdk:11
VOLUME /tmp
ADD ./target/bank-account-service-0.0.1-SNAPSHOT.jar bank-account.jar
ENTRYPOINT ["java","-jar","bank-account.jar"]