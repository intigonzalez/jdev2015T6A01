Media-home
==========

Projet S9 : Enseirb : Réseaux social décentralisé avec partage de videos


# Deployement #

To compile and package the application for single jar deployement, run


    mvn clean package assembly:single

then, you can deploy the all-in-one jar file and run it with just the jvm

   java -jar ./media-home-1.0-SNAPSHOT-jar-with-dependencies.jar


# Deploy Celery with RabbitMQ
sudo apt-get install rabbitmq-server
sudo apt-get install python-celery python-pip
sudo pip install celery
To start celery : celery -A tasks worker --loglevel=info --concurrency=1
If Segfault  sudo apt-get remove python-librabbitmq


# API #

## /api/account ##

### POST ###

Create a user
	
	{
	  "user": {
	    "userID": "userIddD",
	    "name": "name",
	    "surname": "surname",
	    "password": "password",
	    "pubKey": "pubKey",
	    "privateKey": "privateKey"
	  }
	}

	<user>
	  <userID>userIddD</userID>
	  <name>name</name>
	  <surname>surname</surname>
	  <password>password</password>
	  <pubKey>pubKey</pubKey>
	  <privateKey>privateKey</privateKey>
	</user>



