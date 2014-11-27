#!/bin/bash
#Le $1 correspond au fichier entrant dans le transcodeur
#Le $2 correspond au fichier de sortie (uniquement le nom sans l'extension qui est mp4 de toute manière)

echo $1
WIDTH=$( mediainfo $1 | grep Width | cut -d ':' -f 2 | sed 's/[^0-9]//g' )
HEIGHT=$( mediainfo $1 | grep Height | cut -d ':' -f 2 | sed 's/[^0-9]//g' )
echo "Width : $WIDTH"
echo "Height : $HEIGHT"
BITRATE_HIGH=3000
BITRATE_MEDIUM=1500
BITRATE_LOW=200
# Ratio
HEIGHT1=240
WIDTH1=$(($HEIGHT1*$WIDTH/$HEIGHT))
if [[ $(($WIDTH1 % 2)) == 1 ]]; then
WIDTH1=$(($WIDTH1+1))
fi
echo "Width 1: $WIDTH1"
echo "Height 1: $HEIGHT1"
HEIGHT2=480
WIDTH2=$(($HEIGHT2*$WIDTH/$HEIGHT))
if [[ $(($WIDTH2 % 2)) == 1 ]]; then
WIDTH2=$(($WIDTH2+1))
fi
echo "Width 2 : $WIDTH2"
echo "Height 2 : $HEIGHT2"
HEIGHT3=720
WIDTH3=$(($HEIGHT3*$WIDTH/$HEIGHT))
if [[ $(($WIDTH3 % 2)) == 1 ]]; then
WIDTH3=$(($WIDTH3+1))
fi
echo "Width 3 : $WIDTH3"
echo "Height 3 : $HEIGHT3"
ffmpeg -i $1 -vcodec mjpeg -vframes 1 -an -f rawvideo -s 426x240 -ss 20 folder.jpg
#Encoding
ffmpeg -i $1 -c:v libx264 -profile:v main -level 3.1 -b:v ${BITRATE_HIGH}k -vf scale=$WIDTH3:$HEIGHT3 -c:a aac -strict -2 -force_key_frames expr:gte\(t,n_forced*4\) $2-3.mp4
ffmpeg -i $1 -c:v libx264 -profile:v main -level 3.1 -b:v ${BITRATE_MEDIUM}k -vf scale=$WIDTH2:$HEIGHT2 -c:a aac -strict -2 -force_key_frames expr:gte\(t,n_forced*4\) $2-2.mp4
ffmpeg -i $1 -c:v libx264 -profile:v main -level 3.1 -b:v ${BITRATE_LOW}k -vf scale=$WIDTH1:$HEIGHT1 -c:a aac -strict -2 -force_key_frames expr:gte\(t,n_forced*4\) $2-1.mp4
# Chunks HLS
mkdir hls-1 hls-2 hls-3
ffmpeg -i $2-3.mp4 -map 0 -flags +global_header -vcodec copy -vbsf h264_mp4toannexb -acodec copy -f segment -segment_format mpegts -segment_time 4 -segment_wrap 0 -segment_list hls-3/playlist.m3u8 hls-3/chunks_name%03d.ts
ffmpeg -i $2-2.mp4 -map 0 -flags +global_header -vcodec copy -vbsf h264_mp4toannexb -acodec copy -f segment -segment_format mpegts -segment_time 4 -segment_wrap 0 -segment_list hls-2/playlist.m3u8 hls-2/chunks_name%03d.ts
ffmpeg -i $2-1.mp4 -map 0 -flags +global_header -vcodec copy -vbsf h264_mp4toannexb -acodec copy -f segment -segment_format mpegts -segment_time 4 -segment_wrap 0 -segment_list hls-1/playlist.m3u8 hls-1/chunks_name%03d.ts
# WIDTH1=$( mediainfo $2-1.mp4 | grep Width | cut -d ':' -f 2 | sed 's/[^0-9]//g' )
# HEIGHT1=$( mediainfo $2-1.mp4 | grep Height | cut -d ':' -f 2 | sed 's/[^0-9]//g' )
# WIDTH2=$( mediainfo $2-2.mp4 | grep Width | cut -d ':' -f 2 | sed 's/[^0-9]//g' )
# HEIGHT2=$( mediainfo $2-2.mp4 | grep Height | cut -d ':' -f 2 | sed 's/[^0-9]//g' )
# WIDTH3=$( mediainfo $2-3.mp4 | grep Width | cut -d ':' -f 2 | sed 's/[^0-9]//g' )
# HEIGHT3=$( mediainfo $2-3.mp4 | grep Height | cut -d ':' -f 2 | sed 's/[^0-9]//g' )
cat << EOF >> playlist.m3u8
#EXTM3U
#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH=$(($BITRATE_LOW*1000)),RESOLUTION=${WIDTH1}x${HEIGHT1}
hls-1/playlist.m3u8
#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH=$(($BITRATE_MEDIUM*1000)),RESOLUTION=${WIDTH2}x${HEIGHT2}
hls-2/playlist.m3u8
#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH=$((BITRATE_HIGH*1000)),RESOLUTION=${WIDTH3}x${HEIGHT3}
hls-3/playlist.m3u8
#EXT-X-ENDLIST
EOF
# Chunks Dash
MP4Box -dash 4000 -profile onDemand $2-1.mp4#video:id=v1 $2-2.mp4#video:id=v2 $2-3.mp4#video:id=v3 $2-3.mp4#audio:id=a1

#Change Audio Mime Type
mkdir audio
mv $2-3_track2_dashinit.mp4 audio/
echo "AddType audio/mp4 .mp4" > audio/.htaccess

