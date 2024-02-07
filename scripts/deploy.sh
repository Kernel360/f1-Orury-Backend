#!/bin/bash
BUILD_JAR=$(ls /home/ec2-user/orury/orury-client/build/libs/orury-client-0.0.1-SNAPSHOT.jar)
BATCH_JAR=$(ls /home/ec2-user/orury/orury-batch/build/libs/orury-batch-0.0.1-SNAPSHOT.jar)

JAR_NAME=$(basename $BUILD_JAR)
BATCH_JAR_NAME=$(basename $BATCH_JAR)

# 로그 디렉토리 생성 명령어 추가
LOG_DIR="/home/ec2-user/orury/log"
mkdir -p $LOG_DIR

echo "> 현재 시간: $(date)" >> /home/ec2-user/orury/log/deploy_success.log
echo "> 현재 시간: $(date)" >> /home/ec2-user/orury/log/batch_success.log

echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/orury/log/deploy_success.log
echo "> batch build 파일명: $JAR_NAME" >> /home/ec2-user/orury/log/batch_success.log

echo "> build 파일 복사" >> /home/ec2-user/orury/log/deploy_success.log
echo "> build 파일 복사" >> /home/ec2-user/orury/log/batch_success.log
DEPLOY_PATH=/home/ec2-user/
cp $BUILD_JAR $DEPLOY_PATH
cp $BATCH_JAR $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/orury/log/deploy_success.log
echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/orury/log/batch_success.log
CURRENT_PID=$(pgrep -f $JAR_NAME)
BATCH_PID=$(pgrep -f $BATCH_JAR_NAME)

if [ -z "$CURRENT_PID" ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/orury/log/deploy_success.log
else
  echo "> kill -9 $CURRENT_PID" >> /home/ec2-user/orury/log/deploy_success.log
  sudo kill -9 $CURRENT_PID
  sleep 5
fi

if [ -z "$BATCH_PID" ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/orury/log/batch_success.log
else
  echo "> kill -9 $BATCH_PID" >> /home/ec2-user/orury/log/batch_success.log
  sudo kill -9 $BATCH_PID
  sleep 5
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
DEPLOY_BATCH_JAR=$DEPLOY_PATH$BATCH_JAR_NAME

echo "> DEPLOY_JAR 배포"    >> /home/ec2-user/orury/log/deploy_success.log
echo "> BATCH_JAR 배포"    >> /home/ec2-user/orury/log/batch_success.log

cd /home/ec2-user/orury

sudo docker-compose up -d
sudo nohup java -jar $DEPLOY_JAR >> /home/ec2-user/orury/log/tomcat.log 2>/home/ec2-user/orury/log/deploy_error.log &
sudo nohup java -jar $DEPLOY_BATCH_JAR >> /home/ec2-user/orury/log/batch.log 2>/home/ec2-user/orury/log/batch_error.log &