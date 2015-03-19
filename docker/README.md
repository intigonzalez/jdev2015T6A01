# mediahomedocker

Install docker (we need to ask David to get right for get mediahomedocker)

	sudo docker pull mongo
	sudo docker pull dngroup/media-home-box
	sudo docker pull dngroup/media-home-worker

Run

	sudo docker rm -f db && sudo docker run --name db mongo
	sudo docker rm -f worker && sudo docker run --name worker -v /var/www/html:/var/www/html dngroup/media-home-worker
	
	sudo docker rm -f web && sudo docker run  -P --name box --link db:db dngroup/media-home-box java -jar dvd2c-box.jar --ip 0.0.0.0 -p 9998 --db-hostname db  --db-port 27017 -b BOX_DOCKER --content-path /var/www/html -c http://central:9999 -a http://web:9998
	



	
Stop docker

	sudo docker stop db
	sudo docker stop web
	sudo docker pull mongo
	
for dev :
	
	sudo docker build -t worker docker/worker
	sudo docker build -t box docker/box
	sudo docker build -t central docker/central	
	
	sudo docker rm -f db && \
	sudo docker run --name db \
	-v /data/db:/data/db \
	mongo 
	
	sudo docker rm -f worker && \
	sudo docker run --name worker \
	-v /var/www/html:/var/www/html \
	-v /tmp:/tmp \
	worker
	
	sudo docker rm -f central && \
	sudo docker run  -P --name central \
	-v /var/www/html:/var/www/html \
	-p 9999:9999 \
	--link db:db \
	box \
	java -jar dvd2c-central.jar --db-hostname db --db-port 27017
	
	sudo docker rm -f box && \
	sudo docker run  -P --name box \
	-v /var/www/html:/var/www/html \
	-v /tmp:/tmp \
	-p 9998:9998 \
	-p 2080:80 \
	--link db:db \
	--link worker:worker \
	--link central:central \
	box \
	service apache2 restart && \
	java -jar dvd2c-box.jar --ip 0.0.0.0 -p 9998 --db-hostname db -b BOX_DOCKER  -c http://central:9999 -a http://box:9998 --rabbit-host worker
	
and after run apache
	
	sudo docker exec box service apache2 start
	
	
	