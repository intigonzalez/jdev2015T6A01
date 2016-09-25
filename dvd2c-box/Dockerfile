FROM maven:3-jdk-8
MAINTAINER Inti-Gonzalez

# source code
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY . /usr/src/app

# compile source
RUN mvn install -Djavax.xml.accessExternalSchema=all -Dmaven.test.skip=true

# runtime parameters
EXPOSE 8081

# command
ENTRYPOINT ["java", "-jar", "target/dvd2c-box-0.8.0-jar-with-dependencies.jar"]
