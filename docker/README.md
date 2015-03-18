# mediahomedocker

Install docker (we need to ask David to get right for get mediahomedocker)

	sudo docker pull mongo
	sudo docker pull dngroup/media-home-box
	sudo docker pull dngroup/media-home-worker

Run

	sudo docker rm -f db && sudo docker run --name db mongo
	sudo docker rm -f worker && sudo docker run --name worker -v /var/www/html:/var/www/html dngroup/media-home-worker
	
	sudo docker rm -f web && sudo docker run  -P --name web --link db:db dngroup/media-home-box java -jar dvd2c-box.jar --ip 0.0.0.0 -p 9998 --db-hostname db  --db-port 27017 -b BOX_DOCKER --content-path /var/www/html -c http://central:9999 -a http://web:9998
	



	
Stop docker

	sudo docker stop db
	sudo docker stop web
	
	-v /src/webapp:/opt/webapp
	
	