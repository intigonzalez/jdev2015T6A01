FROM debian:stable
MAINTAINER nherbaut@labri.fr
EXPOSE 8080
RUN apt-get update
RUN apt-get install build-essential python-dev python-pip apt-utils gunicorn --yes 
RUN pip install flask
RUN apt-get install dnsutils --yes
COPY . /opt/frontend
WORKDIR /opt/frontend
CMD  export BOX_URI="http://"$BOX_PORT_8081_TCP_ADDR":"$BOX_PORT_8081_TCP_PORT &&  gunicorn --log-level debug --log-file - -b $HOSTNAME:8080 "frontend:bootstrap(abox_uri='"${BOX_URI}"')"

