FROM eclipse-temurin:17-jre
MAINTAINER Wang Chen Chen<932560435@qq.com>
ENV VERSION 1.1
# 挂载容器目录
# tmp: 临时文件目录，内嵌tomcat启动时，用到
# logs: spring-boot 日志输出的目录
# config: spring-boot 外部化配置文件目录
VOLUME ["/tmp","/logs","/config"]
# 复制打包 完成后的jar文件，名字修改成 app.jar
COPY target/molly-service-1.0.1.jar app.jar
# 设置时区为上海
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
# 设置编码
ENV LANG C.UTF-8
# JVM参数
ENV JVM_OPTS="-server -Xms2g -Xmx2g -XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError"
# 服务端口
ENV SERVER_PORT=18891
# 服务暴露端口PORT
EXPOSE ${SERVER_PORT}
# 启动 Spring Boot App 命令
ENTRYPOINT java ${JVM_OPTS} -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai -Djava.security.egd=file:/dev/./urandom -jar /app.jar
