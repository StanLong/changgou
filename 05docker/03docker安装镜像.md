# docker 安装镜像

# 一、安装mysql

```shell
[root@changgou ~]# docker pull mysql
[root@changgou ~]# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
tomcat              latest              b56d8850aed5        3 days ago          529MB
[root@changgou ~]# docker run -p 3306:3306 --name mysql_changgou -e MYSQL_ROOT_PASSWORD=root -d mysql
33803e4f9753dd07b36975257e30b07584de3c066ea8b1351ca2bb68cac178e0
[root@changgou ~]# docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                               NAMES
33803e4f9753        mysql               "docker-entrypoint.s…"   31 seconds ago      Up 30 seconds       0.0.0.0:3306->3306/tcp, 33060/tcp   mysql_changgou

# -e MYSQL_ROOT_PASSWORD=root : 设置当前mysql实例的密码为root

# 设置mysql容器自动启动
[root@changgou ~]# docker update --restart=always 33803e4f9753
```
这里下载的是mysql最新版本，用cmd连接正常，但是用navicat 去连会出问题，连接协议不一样，有如下两种改法
+ 下载mysql5.7的版本， docker pull mysql:5.7
+ 更改最新版本mysql的连接协议
	~~~shell
  [root@changgou ~]# docker exec -it 33803e4f9753 /bin/bash
	root@8aa5af211ee4:/# mysql -u root -p
  Enter password: 
  Welcome to the MySQL monitor.  Commands end with ; or \g.
  Your MySQL connection id is 8
  Server version: 8.0.27 MySQL Community Server - GPL
  
  Copyright (c) 2000, 2021, Oracle and/or its affiliates.
  
  Oracle is a registered trademark of Oracle Corporation and/or its
  affiliates. Other names may be trademarks of their respective
  owners.
  
  Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
  
  mysql> alter user 'root'@'%' identified with mysql_native_password by 'root';
  Query OK, 0 rows affected (0.00 sec)
  ~~~

# 安装 fastdfs
~~~
[root@changgou ~]# docker pull morunchang/fastdfs
[root@changgou ~]# docker run -d --name tracker --net=host morunchang/fastdfs sh tracker.sh
5ab1650b6cc3ae49c4994fa744fb76844c2c20db6f13ac811c0f0b0de197f096   # 运行tracker
[root@changgou ~]# docker run -d --name storage --net=host -e TRACKER_IP=192.168.235.21:22122 -e GROUP_NAME=group1 morunchang/fastdfs sh storage.sh                                   # 运行storage
a8a3493ad2b1e43c4ec0b722bce69482bd069372a78c00080939f7c0bdd828e4
~~~
在我的环境上防火墙已经关了，所以不用配置开放端口什么的。
morunchang/fastdfs 已经配置好了nginx和nginx的相关插件，不需要再重新配置。

# 安装redis
```
[root@changgou ~]# docker pull redis
[root@changgou ~]# docker images
REPOSITORY           TAG                 IMAGE ID            CREATED             SIZE
redis                latest              44d36d2c2374        2 weeks ago         98.2MB
mysql                latest              791b6e40940c        2 weeks ago         465MB
morunchang/fastdfs   latest              a729ac95698a        3 years ago         460MB
[root@changgou ~]# docker run -itd --name changgou_redis -p 6379:6379 redis
```

# 安装 Elasticsearch

```shell
下载
[root@changgou ~]# docker pull elasticsearch
安装
[root@changgou ~]# docker run -di --name=changgou_elasticsearch -p 9200:9200 -p 9300:9300 elasticsearch
eee8bb3d5248a8e0cfccc7e94e29e56c0fd47919c172e98799cb283da8557682
```

9200 端口（Web管理平台端口） 9300端口（服务默认端口）

安装完成后在浏览器里访问地址  http://192.168.235.21:9200/