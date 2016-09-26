FROM ubuntu:latest
MAINTAINER Inti-Gonzalez

# dependencies
RUN apt-get update && apt-get --yes install git

RUN git clone  https://github.com/gpac/gpac --branch v0.5.2 --single-branch
RUN git clone https://github.com/FFmpeg/FFmpeg.git --branch release/2.8 --single-branch

WORKDIR "/FFmpeg"

RUN apt-get install --yes yasm libass-dev libmp3lame-dev libopus-dev libtheora-dev libvorbis-dev libvpx-dev libx264-dev

RUN PKG_CONFIG_PATH="/usr/local/lib/pkgconfig" ./configure \
  --prefix="/usr/local" \
  --extra-cflags="-I/usr/local/include" \
  --extra-ldflags="-L/usr/local/lib" \
  --bindir=/usr/local/bin \
  --enable-gpl \
  --enable-pic \
  --enable-libass \
  --enable-libfreetype \
  --enable-libmp3lame \
  --enable-libopus \
  --enable-libtheora \
  --enable-libvorbis \
  --enable-libvpx \
  --enable-libx264 \
  --enable-nonfree && make && make install && make distclean

WORKDIR "../gpac"
RUN ./configure --use-ffmpeg=no && make && make install

# python dependencies
RUN apt-get install python-pip --yes && pip install --upgrade pip
RUN pip install celery requests pika pymediainfo lxml
# RUN apt-get install libxml2-dev libxslt-dev python-dev --yes
# RUN pip install lxml
RUN apt-get install mediainfo --yes
RUN mkdir -p /var/www/in/
RUN mkdir -p /var/www/out/

# source code
COPY . /usr/src/app
WORKDIR /usr/src/app

# compile sources
RUN python ./setup.py bdist_egg
RUN easy_install /usr/src/app/dist/vhg_adaptation_worker-0.1-py2.7.egg

# clean up
RUN apt-get purge --yes --auto-remove git

# runtime parameters
# require environment variable AMQP_PORT_5672_TCP_ADDR
CMD export C_FORCE_ROOT=true && ./wait-for-it.sh broker:5672 -- celery -A adaptation.commons worker --loglevel=info --concurrency=1



