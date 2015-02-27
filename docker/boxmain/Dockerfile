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
RUN apt-get -y install openjdk-7-jre openjdk-7-jdk apache2 # mongodb-server git maven
 
#create dir /etc/mediahome
RUN mkdir /etc/mediahome
 
# create file box and server configuration
RUN touch central.properties
RUN touch box.properties



RUN echo "bindIp=0.0.0.0" >>  /etc/mediahome/central.properties && \
    echo "bindPort=9999" >>  /etc/mediahome/central.properties 
    
#RUN cat /etc/mediahome/central.properties

RUN echo "bindIp=0.0.0.0" >>  /etc/mediahome/box.properties && \
 echo "bindPort=9998" >>  /etc/mediahome/box.properties && \
  echo "contentPath=/var/www/html" >>  /etc/mediahome/box.properties && \
   echo "BoxID=BOX_TEST" >>  /etc/mediahome/box.properties && \
    echo "CentralURL=http://localhost:9999" >>  /etc/mediahome/box.properties && \
    echo "PublicAddr=http://localhost" >>  /etc/mediahome/box.properties 



# activate module on apache
RUN a2enmod proxy proxy_http headers


# activate forward port 80 to 9998
RUN touch /etc/apache2/sites-available/mediahome.conf


RUN echo '<VirtualHost *:80>'> /etc/apache2/sites-available/mediahome.conf
RUN echo 'Header always set Access-Control-Allow-Origin "*"' >> /etc/apache2/sites-available/mediahome.conf
RUN echo 'Header always set Access-Control-Allow-Methods "POST, GET, OPTIONS, DELETE, PUT"' >> /etc/apache2/sites-available/mediahome.conf
RUN echo 'Header always set Access-Control-Max-Age "1000"' >> /etc/apache2/sites-available/mediahome.conf
RUN echo 'Header always set Access-Control-Allow-Headers "x-requested-with, Content-Type, origin, authorization, accept, client-security-token, range"' >> /etc/apache2/sites-available/mediahome.conf
RUN echo 'ProxyPass /videos !' >> /etc/apache2/sites-available/mediahome.conf
RUN echo 'ProxyPass / http://localhost:9998/' >> /etc/apache2/sites-available/mediahome.conf
RUN echo 'ProxyPassReverse / http://localhost:9998/' >> /etc/apache2/sites-available/mediahome.conf
RUN echo 'ProxyPreserveHost On' >> /etc/apache2/sites-available/mediahome.conf
RUN echo '</VirtualHost>' >> /etc/apache2/sites-available/mediahome.conf


RUN a2dissite 000-default.conf 
RUN a2ensite mediahome.conf
RUN service apache2 restart

# create folder video for put videos inside 
RUN mkdir /var/www/html/videos
RUN chmod 777 /var/www/html/videos

# get the lasted version of dvd2c-box
RUN apt-get -y install curl
RUN curl -L "http://nexus.homeb.tv:8080/nexus/service/local/artifact/maven/redirect?r=snapshots&g=com/enseirb/telecom/dngroup/dvd2c&a=dvd2c-box&v=LATEST&c=jar-with-dependencies" -o dvd2c-box.jar

