DELAY=1
export name="storage_"$RANDOM
echo "launching docker image $name"
export myip=`ifconfig docker0 | grep 'inet addr:' | cut -d: -f2 | awk '{ print $1}'`
echo "stubbing on $myip"
docker run -d -p 8082:8082 -e BOX_PORT_8081_TCP_ADDR=$myip -e  BOX_PORT_8081_TCP_PORT=8081  --name $name nherbaut/dummy-storage 

while ! echo exit | nc $myip 8082 > /dev/null;
do
echo "waiting for storage to wake up, reconnecting in "$DELAY;
sleep $DELAY;
DELAY=$((DELAY+1))
done;


echo "launching tests"
./service-test-storage.py
echo "test done"
echo "shuting down docker"
docker stop $name
docker rm $name
