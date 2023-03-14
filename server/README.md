# Spring Boot 3.X + mybatis-plus + liquibase 多租户[多数据库] 解决方案

### 项目介绍

#### 启动脚本
```shell

# 启动
./molly.sh start

# 停止
./molly.sh stop

# 重启
./molly.sh restart

# 状态
./molly.sh status

```


#### 创建服务
```shell

# 拷贝 molly.service
sudo cp molly.service /usr/lib/systemd/system/

# 设置 molly 服务开机自启动
sudo systemctl enable molly

# 停止 molly 服务开机自启动
sudo systemctl disable molly

# 重启
sudo systemctl daemon-reload

# 启动
sudo systemctl start molly

# 停止
sudo systemctl stop molly

# 重启
sudo systemctl restart molly

# 重启
sudo systemctl status molly

```



#### 全局更新 pom.xml
全局更新 pom.xml 版本 IDEA 按两下 Ctrl

```shell

mvn versions:set -DnewVersion=1.0.2 -DgenerateBackUpPoms=false

```


#### [jdk下载地址](https://adoptium.net/zh-CN/temurin/releases)

