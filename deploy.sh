#!/bin/bash
#Script for Ubuntu 14.04

#Install Java maven apache mongodb and Git
apt-get install openjdk-7-jre openjdk-7-jdk maven apache2 mongodb-server git
 
 #create dir /etc/mediahome
mkdir /etc/mediahome
 
# creat file box and server configuration
touch central.properties
touch box.properties

echo "bindIp=0.0.0.0
bindPort=9999" > /etc/mediahome/central.properties

echo "bindIp=0.0.0.0
bindPort=9998
contentPath=/var/www/html
BoxID=BOX_TEST
CentralURL=http://localhost:9999
PublicAddr=http://localhost" > /etc/mediahome/box.properties

# activate module on apache
a2enmod proxy proxy_http headers


# activate forward port 80 to 9998
touch /etc/apache2/sites-available/mediahome.conf

echo '
<VirtualHost *:80>
DocumentRoot /var/www/html

Header always set Access-Control-Allow-Origin "*"
Header always set Access-Control-Allow-Methods "POST, GET, OPTIONS, DELETE, PUT"
Header always set Access-Control-Max-Age "1000"
Header always set Access-Control-Allow-Headers "x-requested-with, Content-Type, origin, authorization, accept, client-security-token, range"

ProxyPass /videos !
ProxyPass / http://localhost:9998/
ProxyPassReverse / http://localhost:9998/
ProxyPreserveHost On
</VirtualHost>

# vim: syntax=apache ts=4 sw=4 sts=4 sr noet
'> /etc/apache2/sites-available/mediahome.conf

a2dissite 000-default.conf 
a2ensite mediahome.conf
service apache2 reload
service apache2 restart

# create folder video for put videos inside 
mkdir /var/www/html/videos
chmod 777 /var/www/html/videos

# compile 
mvn clean package 
