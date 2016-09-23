FROM openjdk:8-jdk
MAINTAINER Inti-Gonzalez

# dependencies
RUN apt-get update
RUN apt-get install maven --yes
RUN apt-get autoremove

# source code
COPY . /opt/box
WORKDIR /opt/box

# compile source
RUN cd /opt/box && mvn install

CMD ls -l
