# Media@home web application Docker image
#
# VERSION       1.0

# ~~~~ Image base ~~~~
FROM ubuntu:trusty
MAINTAINER David Bourasseau <dbourasseau@viotech.net>


# ~~~~ OS Maintenance ~~~~
# Keep up-to-date the container OS
RUN apt-get update && apt-get upgrade -y

# ~~~~  ~~~~

#Install Java maven apache mongodb and Git
RUN apt-get -y install software-properties-common python-software-properties
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN apt-get -y install oracle-java8-installer 


# get the lasted version of dvd2c-central
RUN apt-get -y install curl
RUN curl -L "http://nexus.homeb.tv:8080/nexus/service/local/artifact/maven/redirect?r=snapshots&g=com/enseirb/telecom/dngroup/dvd2c&a=dvd2c-central&v=LATEST&c=jar-with-dependencies" -o dvd2c-central.jar

