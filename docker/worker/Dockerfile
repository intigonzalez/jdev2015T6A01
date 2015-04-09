# Media@home web application Docker image
#
# VERSION       1.0

# ~~~~ Image base ~~~~
FROM ubuntu:trusty
MAINTAINER David Bourasseau <dbourasseau@viotech.net>


# ~~~~ OS Maintenance ~~~~
# Keep up-to-date the container OS
RUN echo deb http://archive.ubuntu.com/ubuntu trusty universe multiverse >> /etc/apt/sources.list
RUN apt-get update 
RUN apt-get -y install wget libfaac-dev libgpac-dev \
  libmp3lame-dev libopencore-amrnb-dev libopencore-amrwb-dev librtmp-dev libtheora-dev \
  libvorbis-dev pkg-config texi2html yasm zlib1g-dev libxv-dev libva-dev wget libjack-dev libx264-dev libvpx-dev libopus-dev
RUN apt-get -y install libxcb-xfixes0-dev libasound2 libsdl1.2-dev
RUN apt-get -y install rabbitmq-server python-pip \
  mediainfo python-lxml python-dev wget yasm autoconf automake build-essential libass-dev
RUN apt-get -y install libfreetype6-dev libgpac-dev \
  libsdl1.2-dev libtheora-dev libtool libva-dev libvdpau-dev libvorbis-dev libx11-dev \
  libxext-dev libxfixes-dev pkg-config texi2html zlib1g-dev yasm libx264-dev
RUN apt-get -y install libmp3lame-dev libopus-dev libvpx-dev subversion make pkg-config g++ zlib1g-dev libfreetype6-dev \
  libjpeg62-dev libpng12-dev libopenjpeg-dev libmad0-dev libfaad-dev libogg-dev libvorbis-dev \
  libtheora-dev liba52-0.7.4-dev libavcodec-dev libavformat-dev libavutil-dev libswscale-dev 
RUN apt-get -y install libavresample-dev libxv-dev x11proto-video-dev libgl1-mesa-dev x11proto-gl-dev linux-sound-base \
  libxvidcore-dev libssl-dev libjack-dev libasound2-dev libpulse-dev libsdl1.2-dev dvb-apps \
  libavcodec-extra libavdevice-dev libmozjs185-dev libfdk-aac-dev 

#!/bin/bash
#Script for Ubuntu 14.04
RUN wget http://brown:8080/job/FFmpeg/lastSuccessfulBuild/artifact/ffmpeg_2.6-1_amd64.deb
RUN wget http://brown:8080/job/MP4Box/lastSuccessfulBuild/artifact/gpac_0.5.2-1_amd64.deb
RUN apt-get -y install 


RUN dpkg -i ffmpeg_2.6-1_amd64.deb
RUN dpkg -i gpac_0.5.2-1_amd64.deb


# Test version
RUN ffmpeg -version
RUN MP4Box -version

# ~~~~ Clone repo ~~~~


RUN wget http://jenkins.homeb.tv:8080/job/vhg-adaptation-worker/lastSuccessfulBuild/artifact/dist/vhg_adaptation_worker-0.1-py2.7.egg
RUN easy_install vhg_adaptation_worker-0.1-py2.7.egg
EXPOSE 5672
ENTRYPOINT service rabbitmq-server restart && celery -A adaptation.commons worker --loglevel=info --concurrency=1
