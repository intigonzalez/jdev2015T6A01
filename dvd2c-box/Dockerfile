FROM maven:3-jdk-8
MAINTAINER Inti-Gonzalez

# dependencies
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive && apt-get install --yes -q --no-install-recommends mysql-server 

# source code
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY . /usr/src/app

# compile source
RUN mvn install -Djavax.xml.accessExternalSchema=all -Dmaven.test.skip=true

# a database
RUN service mysql start && mysql -u root -e "create database mediahome;"

# runtime parameters
EXPOSE 8081

# command
CMD echo "pepe $BROKER_PORT_5672_TCP_ADDR" && service mysql start && ./wait-for-it.sh broker:5672 -- java -jar target/dvd2c-box-0.8.0-jar-with-dependencies.jar -i `getent hosts $HOSTNAME | cut -d ' ' -f1` -p 8081 --rabbit-host broker --rabbit-port 5672 --database_url jdbc:mysql://localhost:3306/mediahome --database_username root
