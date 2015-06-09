#!/bin/bash

root_dir=~/docker/media-home/
sm_docker_dir=$root_dir"docker/composer/composerSnapmail/"

cd $sm_docker_dir

docker-compose stop
docker-compose rm
docker-compose build --no-cache
docker-compose up
