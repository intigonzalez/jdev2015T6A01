#!/bin/bash
#Script for Ubuntu 14.04

#Install Java maven couchbase and Git
sudo add-apt-repository -y ppa:webupd8team/java
sudo apt-get update
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
sudo apt-get -y install oracle-java8-installer maven apache2 mongodb-server git
echo javax.xml.accessExternalSchema = all | sudo tee  /usr/lib/jvm/java-8-oracle/jre/lib/jaxp.properties > /dev/null
wget http://packages.couchbase.com/releases/3.0.1/couchbase-server-community_3.0.1-ubuntu12.04_amd64.deb
sudo dpkg -i couchbase-server-community_3.0.1-ubuntu12.04_amd64.deb


# create folder video for put videos inside 
mkdir /var/www/html/
chmod 777 /var/www/html/

# compile 
mvn clean package 
