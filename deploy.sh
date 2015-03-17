#!/bin/bash
#Script for Ubuntu 14.04

#Install Java maven apache mongodb and Git
sudo add-apt-repository -y ppa:webupd8team/java
sudo apt-get update
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
sudo apt-get -y install oracle-java8-installer maven apache2 mongodb-server git
 
 
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
