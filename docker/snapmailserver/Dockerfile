# Media@home box application Docker image
#
# VERSION       1.0

# ~~~~ Image base ~~~~
FROM dockerfile/java:oracle-java8
MAINTAINER David Bourasseau <dbourasseau@viotech.net>


RUN mkdir -p /etc/mediahome
#ADD box.properties /etc/mediahome/
EXPOSE 9997


# get the lasted version of dvd2c-box
ADD http://nexus.homeb.tv:8080/nexus/service/local/artifact/maven/redirect?r=snapshots&g=fr/enseirb-matmeca/telecom/t2/s8&a=snapmailserver&v=LATEST&c=jar-with-dependencies snapmailserver.jar
CMD sleep 3 && java -jar snapmailserver.jar 
