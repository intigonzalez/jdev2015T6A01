FROM ubuntu:trusty


RUN mkdir -p /usr/local

RUN apt-get update
RUN apt-get -y install autoconf automake build-essential libass-dev libfreetype6-dev libgpac-dev \
  libsdl1.2-dev libtheora-dev libtool libva-dev libvdpau-dev libvorbis-dev libx11-dev \
  libxext-dev libxfixes-dev pkg-config texi2html zlib1g-dev yasm libx264-dev  libmp3lame-dev libopus-dev libvpx-dev
RUN mkdir ~/ffmpeg_sources
run apt-get --yes install git

RUN git clone  https://github.com/gpac/gpac --branch v0.5.2 --single-branch
RUN git clone https://github.com/FFmpeg/FFmpeg.git --branch release/2.6 --single-branch
WORKDIR "/FFmpeg"

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
  --enable-nonfree

RUN make
RUN make install
RUN make distclean

WORKDIR "../gpac"
RUN ./configure --use-ffmpeg=no 
RUN make && make install




RUN apt-get install dnsutils python-dev python-setuptools --yes
RUN apt-get install python-pip --yes
RUN pip install celery requests pika 
RUN pip install pymediainfo
RUN apt-get install libxml2-dev libxslt-dev python-dev --yes
RUn pip install lxml
RUn apt-get install mediainfo --yes
RUN mkdir -p /var/www/in/
RUN mkdir -p /var/www/out/
VOLUME ["/worker"]
COPY ./run-within-docker.sh /worker/
COPY ./vhg_adaptation_worker-0.1-py2.7.egg /worker/
RUN easy_install /worker/vhg_adaptation_worker-0.1-py2.7.egg
CMD /worker/run-within-docker.sh
