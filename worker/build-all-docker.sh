./build_egg.sh
cp ./dist/*.egg ./docker
sudo docker build -t nherbaut/worker ./docker
echo "docker image nherbaut/worker created"
