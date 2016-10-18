FROM ubuntu:latest
MAINTAINER Inti-Gonzalez

# dependencies
RUN apt-get update
RUN apt-get install python-pip dnsutils iputils-ping --yes
RUN pip install --upgrade pip
RUN pip install flask
# add gunicorn for making it production ready (nginx should also be added)

# source code
COPY . /opt/frontend
WORKDIR /opt/frontend

# runtime parameters
EXPOSE 8080
CMD addr=`ping -c 1 box | grep "PING" | grep -Eo '[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}'` && echo "$addr" && ./wait-for-it.sh box:8081 -- python frontend.py "http://$addr:8081"
