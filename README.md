Media-home
==========

Projet S9 : Enseirb : Réseaux social décentralisé avec partage de videos


# Deployement #

## Application ##


### First install :

	sudo add-apt-repository ppa:webupd8team/java
	sudo apt-get update
	echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
	sudo apt-get install oracle-java8-installer maven git mongodb-server
	echo javax.xml.accessExternalSchema = all | sudo tee  /usr/lib/jvm/java-8-oracle/jre/lib/jaxp.properties > /dev/null
	
	sudo chown -R {username}:{username} /var/www/html

#### We need vhg-adaptation-worker
	
	git clone git@github.com:dngroup/vhg-adaptation-worker.git
	
	sudo ./deploy.sh # is long
	
	
#### change with your configuration

	sudo nano /etc/mediahome/box.properties
	sudo nano /etc/mediahome/central.properties

### Run Application

#### on vhg-adaptation-worker
	
	./start.sh

##### To run the application for development and using default value run :

    mvn clean package
    java -jar ./dvd2c-box/target/dvd2c-box-1.0-SNAPSHOT-jar-with-dependencies.jar 

##### Or for the central server

	java -jar ./dvd2c-central/target/dvd2c-central-1.0-SNAPSHOT-jar-with-dependencies.jar 

##### To run the application for real test, run :

    java -jar ./dvd2c-box/target/dvd2c-box-1.0-SNAPSHOT-jar-with-dependencies.jar  --ip 0.0.0.0 -p 9998 --db-hostname localhost --db-port 27017 -b BOX_TEST --content-path /var/www/html -c http://localhost:9999 -a http://localhost:9998  --rabbit-host localhost --rabbit-port 5672
    
##### or for the central server

	java -jar ./dvd2c-central/target/dvd2c-central-1.0-SNAPSHOT-jar-with-dependencies.jar --ip 0.0.0.0 -p 9999 --db-hostname localhost --db-port 27017

# API #

## /api/account ##

### POST ###

#### Create a user ####

	{
	  "user": {
	    "userID": "user1@onehear.nl",
	    "name": "name",
	    "surname": "surname",
	    "password": "password",
	    "pubKey": "pubKey",
	    "privateKey": "privateKey"
	  }
	}

	<user>
	  <userID>userID</userID>
	  <name>name</name>
	  <surname>surname</surname>
	  <password>password</password>
	  <pubKey>pubKey</pubKey>
	  <privateKey>privateKey</privateKey>
	</user>

#### Create a relation ####

Relationships have an Approuve Value :

1 - I asked the relationships.

2 - I received a request for a relationships

3 - Both accepted the relationship.

	<?xml version="1.0" encoding="UTF-8"?>
	<relation>
	  <email>user1@onehear.nl</email>
	  <name>name</name>
	  <surname>surname</surname>
	  <pubKey>pubKey</pubKey>
	  <aprouve>1</aprouve>
	  <unix_time>0</unix_time>
	  <roleID>0</roleID>
	</relation>

#### Create a box ####

	<box>
	  <boxID>boxID</boxID>
	  <privateKey>privateKey</privateKey>
	  <pubKey>pubKey</pubKey>
	  <ip>localhost:9998</ip>
	  <TTL>0</TTL>
	</box>

#### Edit group of a conten t####
for contentId 54b76bf2-0330-4aa8-99d4-45d05edac051 of vince@onehear.nl
if before you have group 0 and 1 and you want 0 and 4 make
put this uri

	/api/app/vince@onehear.nl/content/54b76bf2-0330-4aa8-99d4-45d05edac051

with

	<?xml version="1.0" encoding="UTF-8"?>
	<content>
	   <authorization>
	    <roleID>0</roleID>
	    <action>action</action>
	  </authorization>
	  <authorization>
	    <roleID>4</roleID>
	    <action>action</action>
	  </authorization>
	</content>

#### edit group of relation ####

for local userId = user1@test.com and relationId = user2@test.com
put this uri

	/api/app/user1@test.com/relation/user2@test.com

	<?xml version="1.0"?>
	<relation>
	  	<groupID>3</groupID>
		<groupID>4</groupID>
	</relation>

#### get list user of a group ####
get this url

	/api/app/{userID}/group/{groupId}

# Other informations #

## Web Interface ##
URL : http://localhost:9998/index.html

## Groups names ##
Groups are only manage with IDs and you cannot change the display, All groups are created manually into JS Code :
0 public
1 family
2 friends
3 work
