# docker 安装镜像

# 一、安装 mysql

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

# 二、安装 FastDFS

```shell
# 查看可用的Fastdfs镜像
[root@changgou ~]# docker search fastdfs
NAME                           DESCRIPTION                                     STARS     OFFICIAL
season/fastdfs                 FastDFS                                         92        
ygqygq2/fastdfs-nginx          fastdfs with nginx/tengine                      34        
morunchang/fastdfs             A FastDFS image                                 20        
delron/fastdfs                                                                 18        
moocu/fastdfs                  fastdfs5.11                                     9         
qbanxiaoli/fastdfs             FastDFS+FastDHT(单机+集群版)                    16        
perfree/fastdfsweb             go-fastdfs文件系统的web管理系统                 2         
luhuiguo/fastdfs               FastDFS is an open source high performance d…   25        
ecarpo/fastdfs-storage                                                         4         
mypjb/fastdfs                  this is a fastdfs docker project                0         
dodotry/fastdfs                更新到最新版本，基于Centos8/nginx1.19.8/Fast…   6         
manuku/fastdfs-fastdht         fastdfs fastdht                                 2         
leaon/fastdfs                  fastdfs                                         1         
manuku/fastdfs-storage-dht     fastdfs storage dht                             0         
manuku/fastdfs-tracker         fastdfs tracker                                 1         
imlzw/fastdfs-tracker          fastdfs的tracker服务                            3         
appcrash/fastdfs_nginx         fastdfs with nginx                              1         
basemall/fastdfs-nginx         fastdfs with nginx                              1         
tsl0922/fastdfs                FastDFS is an open source high performance d…   0         
imlzw/fastdfs-storage-dht      fastdfs的storage服务,并且集成了fastdht的服务…   2         
lionheart/fastdfs_tracker      fastdfs file system‘s tracker node              1         
manuku/fastdfs-storage-proxy   fastdfs storage proxy                           0         
germicide/fastdfs              The image provides  pptx\docx\xlsx to pdf,mp…   0         
ecarpo/fastdfs                                                                 3         
chenfengwei/fastdfs                                                            0         

# 安装 STARS 最多的 season/fastdfs 
[root@changgou ~]# docker pull season/fastdfs 

# 查看已安装的镜像
[root@changgou ~]# docker images
REPOSITORY       TAG       IMAGE ID       CREATED       SIZE
mysql            latest    3218b38490ce   2 years ago   516MB
season/fastdfs   latest    2cfaf455d26c   8 years ago   205MB

# 创建docker容器
[root@changgou ~]# docker run -d --restart=always --name tracker --net=host season/fastdfs sh tracker.sh
f6c6e529e398f020f0b8e228b746871448b8b24de3b45c46f128b6d2ff9adef7

# 创建storage容器
[root@changgou ~]# docker run -d --restart=always --name storage --net=host -e TRACKER_IP=192.168.235.20:22122 -e GROUP_NAME=group1 season/fastdfs sh storage.sh
c89b2b93eb572451ceaa3e161dd6054f955ac1f32f4b0a81d1422c9c6b15b662
- 使用的网络模式是 –net=host, 192.168.235.20是宿主机的IP
- group1是组名，即storage的组  
- 如果想要增加新的storage服务器，再次运行该命令，注意更换 新组名

# 查看docker进程
[root@changgou ~]# docker ps
CONTAINER ID   IMAGE            COMMAND                  CREATED          STATUS                         PORTS                                                  NAMES
c89b2b93eb57   season/fastdfs   "/entrypoint.sh sh s…"   19 seconds ago   Restarting (0) 3 seconds ago                                                          storage
f6c6e529e398   season/fastdfs   "/entrypoint.sh sh t…"   2 minutes ago    Restarting (0) 9 seconds ago                                                          tracker
```

配置FastDFS

```shell

```





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