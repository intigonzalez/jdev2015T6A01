#!/bin/bash

root_dir=~/media-home/
sm_docker_dir=$root_dir"docker/composer/composerSnapmail/"

cd $sm_docker_dir
pwd
sudo docker-compose stop
sudo docker-compose rm
sudo docker-compose build --no-cache
sudo docker-compose up
