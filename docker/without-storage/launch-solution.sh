sudo docker stop broker frontend worker1 box storage
sudo docker rm broker frontend worker1 box storage

sudo docker run -d --name broker -P rabbitmq:3.5.3
sleep 3
sudo docker run -d --name worker1 --link broker:amqp nherbaut/worker
sudo docker run -d --name box -P --link broker:amqp nherbaut/dvd2c-box
sudo docker run -d --name frontend -p 8080:8080 --link box:box nherbaut/frontend 

