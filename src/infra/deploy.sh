#!/bin/bash

REPOSITORY=/home/ubuntu/app/worksmile-message-server
PROJECT_NAME=worksmile-message-server

echo "> 프로젝트 Clone 시작"
cd /home/ubuntu/app
sudo rm -rf $PROJECT_NAME
sudo git clone https://github.com/psh5487/worksmile-message-server.git

echo "> 프로젝트 Build 시작"
cd $REPOSITORY
sudo chmod 777 ./gradlew
sudo ./gradlew clean
sudo ./gradlew build

echo "> 디렉토리 이동"
cd $REPOSITORY

echo "> Build 파일 복사"
sudo cp $REPOSITORY/build/libs/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}*.jar)

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"
if [ -z "$CURRENT_PID" ]; then
   echo "> 현재 구동 중인 애플리케이션이 없습니다."
else
   echo "> kill -15 $CURRENT_PID"
   kill -15 $CURRENT_PID
   sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/ | grep *.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"
nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 &
# java -jar $REPOSITORY/$JAR_NAME