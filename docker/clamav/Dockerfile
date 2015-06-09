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
RUN echo "TCPSocket 3310" > /etc/clamav/clamd.conf
RUN echo "User clamav" >> /etc/clamav/clamd.conf
RUN echo "LogTime yes" >> /etc/clamav/clamd.conf
RUN echo "LogSyslog yes" >> /etc/clamav/clamd.conf
RUN echo "PidFile /var/run/clamd.pid" >> /etc/clamav/clamd.conf
RUN echo "TemporaryDirectory /tmp" >> /etc/clamav/clamd.conf
RUN echo "FixStaleSocket yes" >> /etc/clamav/clamd.conf

EXPOSE 3310

# Run clamav
ENTRYPOINT service clamav-daemon restart && sleep infinity
