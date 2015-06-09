# Media@home web application Docker image
#
# VERSION       1.0

# ~~~~ Image base ~~~~
FROM dockerfile/java:oracle-java8
MAINTAINER David Bourasseau <dbourasseau@viotech.net>


EXPOSE 9999

# get the lasted version of dvd2c-central
ADD http://nexus.homeb.tv:8080/nexus/service/local/artifact/maven/redirect?r=snapshots&g=com/enseirb/telecom/dngroup/dvd2c&a=dvd2c-central&v=LATEST&c=jar-with-dependencies dvd2c-central.jar

CMD sleep 10 && java -jar dvd2c-central.jar --db-hostname db --db-port 27017