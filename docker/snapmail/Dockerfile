# Media@home snapmail application Docker image
#
# VERSION		1.0

# ~~~~ Image base ~~~~
FROM dockerfile/java:oracle-java8
MAINTAINER Maxime Peterlin <mpeterlin@enseirb-matmeca.fr>

# get the lasted version of snapmail
RUN apt-get update

RUN apt-get -y install curl
RUN curl  -L "http://nexus.homeb.tv:8080/nexus/service/local/artifact/maven/redirect?r=snapmail&g=fr/enseirb-matmeca/telecom/t2/s8&a=snapmail&v=LATEST&c=jar-with-dependencies" -o snapmail.jar

ADD TLS /data/TLS

EXPOSE 25004

#CMD java -jar snapmail.jar
