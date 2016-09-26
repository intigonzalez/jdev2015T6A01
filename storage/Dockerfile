FROM maven:3-jdk-8
MAINTAINER Inti-Gonzalez

# source code
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY . /usr/src/app

# compile source
RUN mvn install -Djavax.xml.accessExternalSchema=all -Dmaven.test.skip=true

# runtime parameters
EXPOSE 8082
RUN mkdir -p /var/www/dummy

# command
CMD ./wait-for-it.sh box:8081 -- java -jar target/dummy-storage-1.0-SNAPSHOT-jar-with-dependencies.jar --myPort 8082 --myHostName `getent hosts $HOSTNAME|cut -d' ' -f1` --vanillaStorageHostName box --vanillaStoragePort 8081
