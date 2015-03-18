# Media@home web application Docker image
#
# VERSION       1.0

# ~~~~ Image base ~~~~
FROM ubuntu:trusty
MAINTAINER David Bourasseau <dbourasseau@viotech.net>


# ~~~~ OS Maintenance ~~~~
# Keep up-to-date the container OS
RUN apt-get update && apt-get upgrade -y


#!/bin/bash
#Script for Ubuntu 14.04

# ~~~~ Install RabbitMQ for Celery, Then Celery and MediaInfo wrapper for python  and all dependancy~~~~
RUN echo deb http://archive.ubuntu.com/ubuntu trusty universe multiverse >> /etc/apt/sources.list
RUN apt-get update && apt-get -y install rabbitmq-server python-pip \
  mediainfo python-lxml python-dev wget yasm autoconf automake build-essential libass-dev \
  libfreetype6-dev libgpac-dev \
  libsdl1.2-dev libtheora-dev libtool libva-dev libvdpau-dev libvorbis-dev libx11-dev \
  libxext-dev libxfixes-dev pkg-config texi2html zlib1g-dev yasm libx264-dev libfdk-aac-dev \
  libmp3lame-dev libopus-dev libvpx-dev subversion make pkg-config g++ zlib1g-dev libfreetype6-dev \
  libjpeg62-dev libpng12-dev libopenjpeg-dev libmad0-dev libfaad-dev libogg-dev libvorbis-dev \
  libtheora-dev liba52-0.7.4-dev libavcodec-dev libavformat-dev libavutil-dev libswscale-dev \
  libavresample-dev libxv-dev x11proto-video-dev libgl1-mesa-dev x11proto-gl-dev linux-sound-base \
  libxvidcore-dev libssl-dev libjack-dev libasound2-dev libpulse-dev libsdl1.2-dev dvb-apps \
  libavcodec-extra libavdevice-dev libmozjs185-dev subversion git
  
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
RUN svn co svn://svn.code.sf.net/p/gpac/code/trunk/gpac gpac
WORKDIR gpac
RUN ./configure --use-ffmpeg=no
RUN make 
RUN make install
WORKDIR /


# Test version
RUN ffmpeg -version
RUN MP4Box -version

# ~~~~ Clone repo ~~~~
RUN apt-get -y install 
WORKDIR ~/
RUN git clone https://github.com/dngroup/vhg-adaptation-worker
WORKDIR vhg-adaptation-worker
RUN service rabbitmq-server restart
RUN apt-get install lsof
#ENTRYPOINT celery -A adaptation.commons worker --loglevel=info --concurrency=1

