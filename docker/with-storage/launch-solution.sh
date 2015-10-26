sudo docker stop broker frontend worker1 box storage
sudo docker rm broker frontend worker1 box storage

sudo docker run -d --name broker -P rabbitmq:3.5.3
sleep 3
sudo docker run -d -e "CELERY_BROCKER_URL=amqp://192.168.1.142:5672" --name worker1 --link broker:amqp nherbaut/worker:jdev
sudo docker run -d --name box -P --link broker:amqp nherbaut/dvd2c-box
sudo docker run -d --name frontend -p 8080:8080 --link box:box nherbaut/frontend 
sudo docker run -d --name storage -P --link box:box nherbaut/dummy-storage


