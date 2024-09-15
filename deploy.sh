#!/bin/bash

docker rm -f $(docker ps -a -q --filter "name=backend-application")
docker rmi $(docker images 'backend-application' -a -q)
docker rm -f $(docker ps -a -q --filter "name=migration")
docker rmi $(docker images 'migration' -a -q)

cd db || exit
docker build -t migration .
cd ../ || exit

docker compose up -d db migration keycloak
docker compose up -d