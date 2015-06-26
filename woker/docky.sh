./build_egg.sh
cp ./dist/*.egg ./docker
sudo docker build -t dngroup/adaptation-worker ./docker 
