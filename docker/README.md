# mediahomedocker


## Install docker 

	curl -sSL https://get.docker.com/ubuntu/ | sudo sh
or

	wget -qO- https://get.docker.com/ | sh

## docker from docker hub

### with composer

#### install composer

https://docs.docker.com/compose/install/

	curl -L https://github.com/docker/compose/releases/download/1.1.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
	chmod +x /usr/local/bin/docker-compose

#### use composer
 
	cd docker/composerHub
	sudo docker-compose up
	
if we want a central server on local
	
	sudo docker pull dngroup/central
	
	sudo docker rm -f central || \
	sudo docker run -i -t -P --name central \
	-p 9999:9999 \
	--link db:db \
	dngroup/central
	 
	
### without composer
_no support for this part_ 

#### get docker on local 

	sudo docker pull mongo
	sudo docker pull dngroup/media-home-box
	sudo docker pull dngroup/media-home-worker
	sudo docker pull dngroup/central

#### Run


	sudo docker rm -f db && sudo docker run --name db mongo
	sudo docker rm -f worker && sudo docker run --name worker -v /var/www/html:/var/www/html dngroup/media-home-worker
	
	sudo docker rm -f web && sudo docker run  -P --name box --link db:db dngroup/media-home-box java -jar dvd2c-box.jar --ip 0.0.0.0 -p 9998 --db-hostname db  --db-port 27017 -b BOX_DOCKER --content-path /var/www/html -c http://central:9999 -a http://web:9998
	


#### Stop docker

	sudo docker stop db
	sudo docker stop web
	sudo docker pull mongo
	
	
## docker for dev : 

### with composer

#### install composer

https://docs.docker.com/compose/install/

	curl -L https://github.com/docker/compose/releases/download/1.1.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
	chmod +x /usr/local/bin/docker-compose

#### use composer

	cd docker/composer
	sudo docker-compose up

### without composer 
_no support for this part_ 

	sudo docker build -t worker docker/worker
	sudo docker build -t box docker/box
	sudo docker build -t central docker/central	
	
#### mongo db

	sudo docker rm -f db && \
	sudo docker run -i -t --name db \
	-v /data/db:/data/db \
	mongo 
	

#### worker

	sudo docker rm -f worker && \
	sudo docker run -i -t --name worker \
	-v /var/www/html:/var/www/html \
	-v /tmp:/tmp \
	worker
	
#### box docker

	sudo docker rm -f box && \
	sudo docker run -i -t -P --name box \
	-v /var/www/html:/var/www/html \
	-v /tmp:/tmp \
	-p 9998:9998 \
	--link db:db \
	--link worker:worker \
	box # java -jar dvd2c-box.jar --db-hostname db -b BOX_DOCKER  -c http://central:9999 -a http://box:9998 --rabbit-host worker

	
#### central docker (you need mongo)

	sudo docker rm -f central && \
	sudo docker run -i -t -P --name central \
	-p 9999:9999 \
	--link db:db \
	central # java -jar dvd2c-central.jar --db-hostname db --db-port 27017

	
## automoatique reload docker


add your root shh-key on your github

	sudo su
	ssh-keygen
	ctrl + D
	
creat and edit this file
	
	nano ~/reload.sh
	
copy this

	#!/bin/bash

	cd /home/user/Media-home
	git pull
	cd docker/composer/composerBoxHub
	sudo docker stop composerboxhub_box_1 composerboxhub_worker_1 composerboxhub_db_1
	sudo docker-compose rm --force
	sudo docker-compose pull
	sudo docker-compose up

run

	chmod +x ~/reload.sh
	
create cron
	
	sudo crontab -e
	
add this and change "user" by your user name

	0 4 * * * /home/user/reload.sh
	

	