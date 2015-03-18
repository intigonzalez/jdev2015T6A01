# mediahomedocker

Install docker (we need to ask David to get right for get mediahomedocker)

	sudo docker pull mongo
	sudo docker pull dngroup/media-home-box

Run

	sudo docker run --name db mongo
	sudo docker run  -P --name web --link db:db dbourasseau/mediahomedocker java -jar dvd2c-box.jar --ip 0.0.0.0 -p 9998 --db-hostname db  --db-port 27017 -b BOX_DOCKER --content-path /var/www/html -c http://central:9999 -a http://web:9998
	
Delete link before re running
	
	sudo docker rm -f db
	sudo docker rm -f web
	
Stop docker

	sudo docker stop db
	sudo docker stop web