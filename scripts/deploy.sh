#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=hellomyteam #해당 위치에 properties에 작성한 프로젝트명과 동일하게 작성합니다.

echo "> Build 파일 복사" >> ./log.txt
cp $REPOSITORY/zip/build/libs/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인" >> ./log.txt
CURRENT_PID=$(pgrep -f $PROJECT_NAME)

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID" >> ./log.txt

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다" >> ./log.txt
else
  echo "> kill -15 $CURRENT_PID" ./log.txt
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포" >> ./log.txt
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR_NAME: $JAR_NAME" >> ./log.txt
echo "> $JAR_NAME 에 실행권한 추가">> ./log.txt


echo "> $JAR_NAME 실행" >> ./log.txt
nohup java -jar \
	-Dspring.config.location=classpath:/application.yml,/home/ec2-user/app/step2/application-oauth.yml \
	-Dspring.profiles.active=dev &

echo ">> finish" >> ./log.txt