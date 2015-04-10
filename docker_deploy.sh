#!/bin/bash
#Script for Ubuntu 14.04

# Update
sudo apt-get update -y

# Docker + Docker-compose installation
if [[ ! $(which wget) ]] 
then
	sudo apt-get install wget
fi

if [[ (! $(which docker)) && (! $(which docker.io)) ]]
then
	echo "[INSTALL] Docker"
	wget -qO- https://get.docker.com/ | sh
fi

if [[ ! $(which docker-compose) ]]
then
	if [[ ! $(which curl) ]]
	then
		sudo apt-get install curl
	fi

	echo "[INSTALL] Docker-compose"
	curl -L https://github.com/docker/compose/releases/download/1.1.0/docker-compose-`uname -s`-`uname -m` > docker-compose
	sudo mv docker-compose /usr/local/bin/
	sudo chmod +x /usr/local/bin/docker-compose
fi

# Creation of the docker group
if [[ ! $(egrep -i "^docker" /etc/group) ]]
then
	echo "Creation of the group docker"
	sudo groupadd docker
fi

# Add the current user to the group
echo "Current user will be added to the group docker"
sudo gpasswd -a ${USER} docker
sudo service docker restart
newgrp docker

# Creation of the cron job
cronjob = "0 0 * * * sh -c"
if [[ ! $(crontab -l | grep $cronjob) ]]
then
	echo $cronjob
fi
crontab -l
