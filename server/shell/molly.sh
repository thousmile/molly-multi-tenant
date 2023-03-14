#!/bin/bash
#chkconfig:345 61 61
#description: Spring Boot App Script
#auther: Wang Chen Chen

#===========================================================================================
# Document And Example
#===========================================================================================
#用法: ./脚本名.sh {start|stop|restart|status}
#例子(一)：./molly.sh start 					  # 启动
#例子(一)：./molly.sh stop 					  # 停止
#例子(一)：./molly.sh restart 					# 重启
#例子(一)：./molly.sh status 					# 状态

#===========================================================================================
# Spring Boot Application Start Configuration
#===========================================================================================

# Spring Boot App 名称
APP_NAME=/opt/molly-server/molly-service-1.0.1.jar

# spring.profiles.active 配置激活环境
#SPRING_ENV=dev           # 开发环境
SPRING_ENV=pro            # 本地生产环境
#SPRING_ENV=prod          # 线上生产环境
#SPRING_ENV=test          # 测试环境

# JVM参数
JVM_OPTS="-Dname=$APP_NAME -server -Xms4g -Xmx4g -XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai -Djava.security.egd=file:/dev/./urandom"

# JDK路径
JAVA_HOME=/opt/jdk-17

# 第一个参数 start|stop|restart|status 中的一个
if [ "$1" = "" ];
then
	echo -e "\033[0;31m 未输入操作名 \033[0m  \033[0;34m {start|stop|restart|status} \033[0m"
	exit 1
fi


# 启动服务
function start()
{
	PID=`ps -ef |grep java|grep $APP_NAME|grep -v grep|wc -l`
	if [ $PID != 0 ];then
		echo "$APP_NAME is running..."
	else
		echo "start $APP_NAME active: $SPRING_ENV success..." &
		nohup $JAVA_HOME/bin/java -jar $JVM_OPTS $APP_NAME --spring.profiles.active=$SPRING_ENV >/dev/null 2>&1 &
	fi
}


# 停止服务
function stop()
{
	PID=""
	query(){
		PID=`ps -ef |grep java|grep $APP_NAME|grep -v grep|awk '{print $2}'`
	}
	query
	if [ x"$PID" != x"" ]; then
		kill -9 $PID
		echo "$APP_NAME (pid:$PID) exiting..."
		while [ x"$PID" != x"" ]
		do
			sleep 1
			query
		done
		echo "$APP_NAME exited ..."
	else
		echo "$APP_NAME already stopped ..."
	fi
}


# 重启服务
function restart()
{
	stop
	sleep 2
	start
}


# 查看服务状态
function status()
{
	PID=`ps -ef |grep java|grep $APP_NAME|grep -v grep|wc -l`
	if [ $PID != 0 ];then
		echo "$APP_NAME is running..."
	else
		echo "$APP_NAME is stop..."
	fi
}


case $1 in
	start)
		start;;
	stop)
		stop;;
	restart)
		restart;;
	status)
		status;;
	*)

		echo -e "\033[0;31m Usage: \033[0m  \033[0;34m sh  $0  {start|stop|restart|status}  {APP_NAME} \033[0m
		\033[0;31m Example: \033[0m
		\033[0;33m sh  $0  start example.jar \033[0m"
esac

