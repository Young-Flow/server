#!/usr/bin/env bash

PROJECT_ROOT="/home/ec2-user/app"

NEED_RESTART=0
for CONTAINER in pitchain_filebeat pitchain_logstash pitchain_kibana pitchain_elasticsearch; do
  if ! docker ps --format '{{.Names}}' | grep -q "^${CONTAINER}$"; then
    echo "$CONTAINER 컨테이너가 실행 중이 아닙니다."
    NEED_RESTART=1
  fi
done

if [ $NEED_RESTART -eq 1 ]; then
  echo "하나 이상의 ELK 컨테이너가 다운되어 있으므로 ELK 및 Filebeat를 재시작합니다."
  docker-compose --env-file $PROJECT_ROOT/.env --profile setup -f $PROJECT_ROOT/docker-compose-elk.yml up --build -d
else
  echo "모든 ELK 컨테이너가 정상적으로 실행 중입니다."
fi

JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

cd $PROJECT_ROOT

TIME_NOW=$(date +%c)

# build 파일 복사
echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# jar 파일 실행 (prod 프로필 적용)
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE --spring.profiles.active=prod > $APP_LOG 2> $ERROR_LOG &
