Media-home
==========

Projet S9 : Enseirb : Réseaux social décentralisé avec partage de videos


# Deployement #

To compile and package the application for single jar deployement, run


    mvn clean package assembly:single

then, you can deploy the all-in-one jar file and run it with just the jvm

   java -jar ./media-home-1.0-SNAPSHOT-jar-with-dependencies.jar


you need this to work fine :
	sudo apt-get install 

# Deploy Celery with RabbitMQ
	sudo apt-get install rabbitmq-server
	sudo apt-get install python-celery python-pip
	sudo pip install celery
To start celery : celery -A tasks worker --loglevel=info --concurrency=1
If Segfault 
	sudo apt-get remove python-librabbitmq


# API #

## /api/account ##

### POST ###

Create a user
	
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

Create a relation

	<?xml version="1.0" encoding="UTF-8"?>
	<relation>
	  <email>user1@onehear.nl</email>
	  <name>name</name>
	  <surname>surname</surname>
	  <pubKey>pubKey</pubKey>
	  <aprouve>1</aprouve>
	  <unix_time>0</unix_time>
	  <groupID>0</groupID>
	</relation>

Create a box

	<box>
	  <boxID>boxID</boxID>
	  <privateKey>privateKey</privateKey>
	  <pubKey>pubKey</pubKey>
	  <ip>localhost:9998</ip>
	  <TTL>0</TTL>
	</box>
	
Edit group of a content
for contentId 54b76bf2-0330-4aa8-99d4-45d05edac051 of vince@onehear.nl
if before you have group 0 and 1 and you want 0 and 4 make
put this uri

	/api/app/vince@onehear.nl/content/54b76bf2-0330-4aa8-99d4-45d05edac051
	
white

	<?xml version="1.0" encoding="UTF-8"?>
	<content>
	   <authorization>
	    <groupID>0</groupID>
	    <action>action</action>
	  </authorization>
	  <authorization>
	    <groupID>4</groupID>
	    <action>action</action>
	  </authorization>
	</content>
	   
edit group of relation

for local userId = user1@test.com and relationId = user2@test.com
put this uri

	/api/app/user1@test.com/relation/user2@test.com

	<?xml version="1.0"?>
	<relation>
	  	<groupID>3</groupID>
		<groupID>4</groupID>
	</relation>

get list user of a group
get this url
	
	/api/app/{userID}/group/{groupId}

for information group 
0 public
1 family
2 friends
3 work

# Web Interface #
URL : http://localhost:9998/index.html?email=vince@onehear.nl#/home