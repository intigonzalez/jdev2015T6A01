# Media@home snapmail application Docker image
#
# VERSION		1.0

# ~~~~ Image base ~~~~
FROM dockerfile/java:oracle-java8
MAINTAINER Maxime Peterlin <mpeterlin@enseirb-matmeca.fr>

RUN apt-get update

# install ClamAV and clamav-daemon
RUN apt-get install clamav clamav-daemon -y

# Update ClamAV database
RUN freshclam -v

# Change the ClamAV configuration file
RUN echo "TCPSocket 3310" >> /etc/clamav/clamd.conf && \
    echo "TCPAddr 127.0.0.1" >> /etc/clamav/clamd.conf && \
    sed -i '4,5d' /etc/clamav/clamd.conf 

EXPOSE 3310

# Run clamav
ENTRYPOINT service clamav-daemon start && sleep infinity
