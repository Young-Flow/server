#!/usr/bin/env bash

PROJECT_ROOT="/home/ec2-user/app"

cd $PROJECT_ROOT

NEED_RESTART=0
for CONTAINER in pitchain_filebeat pitchain_logstash pitchain_kibana pitchain_elasticsearch; do
  if ! docker ps --format '{{.Names}}' | grep -q "^${CONTAINER}$"; then
    echo "$CONTAINER 컨테이너가 실행 중이 아닙니다."
    NEED_RESTART=1
  fi
done

if [ $NEED_RESTART -eq 1 ]; then
  echo "하나 이상의 ELK 컨테이너가 다운되어 있으므로 ELK 및 Filebeat를 재시작합니다."
  docker-compose --env-file .env --profile setup -f docker-compose-elk.yml up --build -d
else
  echo "모든 ELK 컨테이너가 정상적으로 실행 중입니다."
fi