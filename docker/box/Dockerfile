# Media@home box application Docker image
#
# VERSION       1.0

# ~~~~ Image base ~~~~
FROM dockerfile/java:oracle-java8
MAINTAINER David Bourasseau <dbourasseau@viotech.net>

<<<<<<< HEAD
=======


# get the lasted version of dvd2c-box
RUN apt-get update && apt-get -y install curl
RUN curl -L "http://nexus.homeb.tv:8080/nexus/service/local/artifact/maven/redirect?r=releases&g=com/enseirb/telecom/dngroup/dvd2c&a=dvd2c-box&v=LATEST&c=jar-with-dependencies" -o dvd2c-box.jar

>>>>>>> 0a46773ed42f716ebfb9abb5abc1c9a85a057c21
RUN mkdir -p /etc/mediahome
#ADD box.properties /etc/mediahome/
EXPOSE 9998

# get the lasted version of dvd2c-box
ADD http://nexus.homeb.tv:8080/nexus/service/local/artifact/maven/redirect?r=snapmail&g=com/enseirb/telecom/dngroup/dvd2c&a=dvd2c-box&v=LATEST&c=jar-with-dependencies dvd2c-box.jar
CMD sleep 10 && java -jar dvd2c-box.jar --ip 0.0.0.0 -p 9998 --db-hostname db  --rabbit-host worker
