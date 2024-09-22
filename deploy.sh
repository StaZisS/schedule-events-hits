#!/bin/bash

docker rm -f $(docker ps -a -q --filter "name=schedule-events-hits-application")
docker rmi $(docker images 'schedule-events-hits-application' -a -q)
docker rm -f $(docker ps -a -q --filter "name=migration")
docker rmi $(docker images 'migration' -a -q)

cd db || exit
docker build -t migration .
cd ../ || exit

docker compose up -d db migration
docker compose up -d