#!/bin/bash
#Script for Ubuntu 14.04

#Install Java maven apache mongodb and Git
sudo add-apt-repository -y ppa:webupd8team/java
sudo apt-get update
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
sudo apt-get -y install oracle-java8-installer maven mongodb-server git
echo javax.xml.accessExternalSchema = all | sudo tee  /usr/lib/jvm/java-8-oracle/jre/lib/jaxp.properties > /dev/null
 
# create folder video for put videos inside 
mkdir /var/www/html
chown "$USER":"$USER" -R /var/www/html

# compile 
mvn clean package 
