./build_egg.sh
cp ./dist/*.egg ./docker
sudo docker build -t nherbaut/worker_jdev ./docker
echo "docker image nherbaut/worker created"
