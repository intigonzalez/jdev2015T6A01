docker_pkg=$(dpkg -l | grep docker | tr -s ' ' | cut -d' ' -f2)
for i in $docker_pkg
do
	echo "[UNINSTALL] "$i
	sudo dpkg -P $i
done

echo "[UNINSTALL] docker-compose"
sudo rm -rf /usr/local/bin/docker-compose

echo "[DELETE] group docker"
sudo delgroup docker
