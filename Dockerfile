FROM openjdk:11-jre
WORKDIR /ryver-auth
COPY . /app
ENTRYPOINT ["java","-jar","/app/ryver-auth/target/auth-0.0.1-SNAPSHOT.jar"]