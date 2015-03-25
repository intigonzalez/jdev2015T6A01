# Media@home box application Docker image
#
# VERSION       1.0

# ~~~~ Image base ~~~~
FROM dbourasseau/java8arm
MAINTAINER David Bourasseau <dbourasseau@viotech.net>



# get the lasted version of dvd2c-box
RUN apt-get -y install curl
RUN curl -L "http://nexus.homeb.tv:8080/nexus/service/local/artifact/maven/redirect?r=snapshots&g=com/enseirb/telecom/dngroup/dvd2c&a=dvd2c-box&v=LATEST&c=jar-with-dependencies" -o dvd2c-box.jar
# ENTRYPOINT [ "bash","service","apache2","restart","&&","java","-version"]

EXPOSE 9998

CMD java -jar dvd2c-box.jar --ip 0.0.0.0 -p 9998 --db-hostname db -b BOX_DOCKER  -c http://central:9999 -a http://box:9998 --rabbit-host worker

