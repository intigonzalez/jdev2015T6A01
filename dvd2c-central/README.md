#Media-home Central Server

Réseaux social décentralisé avec partage de videos


# Deployement #

## Application ##

First install :

	sudo add-apt-repository ppa:webupd8team/java
	sudo apt-get update
	echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
	sudo apt-get install oracle-java8-installer maven git
	echo javax.xml.accessExternalSchema = all | sudo tee  /usr/lib/jvm/java-8-oracle/jre/lib/jaxp.properties > /dev/null
	wget http://packages.couchbase.com/releases/3.0.1/couchbase-server-community_3.0.1-ubuntu12.04_amd64.deb
	sudo dpkg -i couchbase-server-community_3.0.1-ubuntu12.04_amd64.deb
 
To run the application for development and using default value run (change **** by the correct version :

    mvn clean package
    java -jar ./dvd2c-central/target/dvd2c-central-****-SNAPSHOT-jar-with-dependencies.jar 

    
or for the central server

	java -jar ./dvd2c-central/target/dvd2c-central-1.0-SNAPSHOT-jar-with-dependencies.jar --ip 0.0.0.0 -p 9999 --db-hostname localhost --db-port 27017



## CouchBase Configuration ##

for configure CouchBase we need :

*   go to this web page http://localhost:8091/ on configure your server
*   create a new bucket "central" on Data Buckets
*   create two new view on /!\"central"/!\ :
 * "_design/dev_boxrepositoryobject" and "_design/dev_userrepositoryobject" with name "all" 
 * for each change map code by :
 for _design/dev_boxrepositoryobject
 
	function (doc, meta) {
	  if (doc._class == "com.enseirb.telecom.dngroup.dvd2c.db.BoxRepositoryObject") {
	    emit(null, null);
	  }
	}
and for _design/dev_userrepositoryobject
	
	function (doc, meta) {
	  if (doc._class == "com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryObject") {
	    emit(null, null);
	  }
	}
	
* after that press on Publish button
 


# Other informations #

## Web Interface ##
URL : http://localhost:9998/index.html

## Groups names ##
Groups are only manage with IDs and you cannot change the display, All groups are created manually into JS Code :
0 public
1 family
2 friends
3 work


