#!/usr/bin/env bash

PROJECT_ROOT="/home/ec2-user/app"
JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"

DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)
sleep 3
CURRENT_PID=$(pgrep -f $JAR_FILE | tail -n 1)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG