FROM ubuntu:latest
MAINTAINER Inti-Gonzalez

# dependencies
RUN apt-get update

RUN apt-get install python-pip --yes
RUN pip install --upgrade pip
RUN pip install flask

# add gunicorn for making it production ready (nginx should also be added)

# source code
COPY . /opt/frontend
WORKDIR /opt/frontend

# runtime parameters
EXPOSE 8080
CMD python frontend.py pepe

