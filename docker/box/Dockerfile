# Media@home box application Docker image
#
# VERSION       1.0

# ~~~~ Image base ~~~~
FROM dockerfile/java:oracle-java8
MAINTAINER David Bourasseau <dbourasseau@viotech.net>



# get the lasted version of dvd2c-box
RUN apt-get update && apt-get -y install curl
RUN curl -L "http://nexus.homeb.tv:8080/nexus/service/local/artifact/maven/redirect?r=snapshots&g=com/enseirb/telecom/dngroup/dvd2c&a=dvd2c-box&v=LATEST&c=jar-with-dependencies" -o dvd2c-box.jar

RUN mkdir -p /etc/mediahome
#ADD box.properties /etc/mediahome/

EXPOSE 9998

CMD java -jar dvd2c-box.jar 

