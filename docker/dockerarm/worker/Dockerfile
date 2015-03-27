# Media@home web application Docker image
#
# VERSION       1.0

# ~~~~ Image base ~~~~
FROM mazzolino/armhf-ubuntu:14.04
MAINTAINER David Bourasseau <dbourasseau@viotech.net>


# ~~~~ OS Maintenance ~~~~
# Keep up-to-date the container OS
RUN apt-get update && apt-get upgrade -y


#!/bin/bash
#Script for Ubuntu 14.04

# ~~~~ Install RabbitMQ for Celery, Then Celery and MediaInfo wrapper for python  and all dependancy~~~~
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
  libavcodec-extra libavdevice-dev libmozjs185-dev subversion git



RUN echo deb http://ports.ubuntu.com/ trusty main restricted universe multiverse  >> /etc/apt/sources.list

RUN apt-get update &&  apt-get -y install libfdk-aac-dev



RUN pip install celery pymediainfo


# ~~~~ Compile FFmpeg ~~~~
RUN mkdir -p /usr/local/

RUN mkdir ~/ffmpeg-source
WORKDIR ffmpeg-source
RUN wget http://ffmpeg.org/releases/ffmpeg-snapshot.tar.bz2
RUN tar xjvf ffmpeg-snapshot.tar.bz2
WORKDIR ffmpeg
RUN PKG_CONFIG_PATH="/usr/local/lib/pkgconfig"

RUN ./configure \
  --prefix="/usr/local/" \
  --extra-cflags="-I/usr/local/include" \
  --extra-ldflags="-L/usr/local/lib" \
  --bindir=/usr/local/bin \
  --enable-gpl \
  --enable-pic \
  --enable-libass \
  --enable-libfdk-aac \
  --enable-libfreetype \
  --enable-libmp3lame \
  --enable-libopus \
  --enable-libtheora \
  --enable-libvorbis \
  --enable-libvpx \
  --enable-libx264 \
  --enable-nonfree

RUN make \
&& make install \
&& make distclean
WORKDIR /

# ~~~~ Compile Manually MP4Box from GPAC project ~~~~
RUN git clone https://github.com/gpac/gpac.git
WORKDIR gpac
RUN ./configure --enable-pic --use-ffmpeg=no
RUN make
RUN make install
WORKDIR /

# Test version
RUN ffmpeg -version
RUN MP4Box -version



# ~~~~ Clone repo ~~~~
RUN apt-get -y install 
WORKDIR ~/

RUN wget http://jenkins.homeb.tv:8080/job/vhg-adaptation-worker/lastSuccessfulBuild/artifact/dist/vhg_adaptation_worker-0.1-py2.7.egg
RUN easy_install vhg_adaptation_worker-0.1-py2.7.egg
EXPOSE 5672
ENTRYPOINT service rabbitmq-server restart && celery -A adaptation.commons worker --loglevel=info --concurrency=1

