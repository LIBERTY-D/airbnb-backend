#!/usr/bin/env bash



#check for  env variables if set
#ENV_FILE=.env docker-compose --env-file .env config
##APP_PORT=9090 ENV_FILE=.env.prod docker-compose --env-file .env.prod up --build
ENV_FILE=.env docker-compose --env-file .env up --build
