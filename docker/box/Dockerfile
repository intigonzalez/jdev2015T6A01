# Media@home box application Docker image
#
# VERSION       1.0

# ~~~~ Image base ~~~~
FROM dockerfile/java:oracle-java8
MAINTAINER David Bourasseau <dbourasseau@viotech.net>


RUN mkdir -p /etc/mediahome
#ADD box.properties /etc/mediahome/
EXPOSE 9998

RUN sudo apt-get update 
RUN sudo apt-get install -y mysql-client

# get the lasted version of dvd2c-box
ADD http://nexus.homeb.tv:8080/nexus/service/local/artifact/maven/redirect?r=snapshots&g=com/enseirb/telecom/dngroup/dvd2c&a=dvd2c-box&v=LATEST&c=jar-with-dependencies dvd2c-box.jar
CMD sleep 3 && java -Dorg.slf4j.simpleLogger.defaultLogLevel=info -jar dvd2c-box.jar --ip 0.0.0.0 -p 9998  --rabbit-host worker
