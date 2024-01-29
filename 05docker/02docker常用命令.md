# Docker 常用命令

官方帮忙文档

https://docs.docker.com/engine/reference/commandline/docker/

```shell
# 帮助命令
docker version       # 显示docker的版本信息
docker info          # 显示 docker 的系统信息，包括镜像和容器的数量
docker 命令 --help    # 帮助命令
```

# 一、镜像命令

**docker images  查看所有本地主机的镜像**

```shell
[root@changgou ~]# docker images 
REPOSITORY    TAG       IMAGE ID       CREATED        SIZE
hello-world   latest    d2c94e258dcb   9 months ago   13.3kB

字段说明：
REPOSITORY 镜像的仓库源
TAG        镜像的标签
IMAGE ID   镜像的ID
CREATED    镜像的创建时间
SIZE       镜像大小

# 可选项
 -a, --all      # 列出所有镜像
 -q, --quiet    # 只显示镜像的ID
 
```

**docker search 搜索镜像**

```shell
[root@changgou ~]# docker search mysql
NAME                            DESCRIPTION                                     STARS     OFFICIAL
mysql                           MySQL is a widely used, open-source relation…   14806     [OK]
mariadb                         MariaDB Server is a high performing open sou…   5653      [OK]

# 可选项 
 --filter=STATS=3000   通过收藏来过滤, 这样搜索到的镜像就是STARS大于3000的
```

**docker pll 下载镜像**

```shell
# 下载镜像 docker pull 镜像名[:tag] 可以指定下载镜像的版本，默认下载最新版本
[root@changgou ~]# docker pull mysql
Using default tag: latest
latest: Pulling from library/mysql
72a69066d2fe: Pull complete 
93619dbc5b36: Pull complete 
99da31dd6142: Pull complete 
626033c43d70: Pull complete 
37d5d7efb64e: Pull complete 
ac563158d721: Pull complete 
d2ba16033dad: Pull complete 
688ba7d5c01a: Pull complete 
00e060b6d11d: Pull complete 
1c04857f594f: Pull complete 
4d7cfa90e6ea: Pull complete 
e0431212d27d: Pull complete 
Digest: sha256:e9027fe4d91c0153429607251656806cc784e914937271037f7738bd5b8e7709
Status: Downloaded newer image for mysql:latest
docker.io/library/mysql:latest

# 指定版本下载
[root@changgou ~]# docker pull mysql:5.7
5.7: Pulling from library/mysql
72a69066d2fe: Already exists 
93619dbc5b36: Already exists 
99da31dd6142: Already exists 
626033c43d70: Already exists 
37d5d7efb64e: Already exists 
ac563158d721: Already exists 
d2ba16033dad: Already exists 
0ceb82207cd7: Pull complete 
37f2405cae96: Pull complete 
e2482e017e53: Pull complete 
70deed891d42: Pull complete 
Digest: sha256:f2ad209efe9c67104167fc609cca6973c8422939491c9345270175a300419f94
Status: Downloaded newer image for mysql:5.7
docker.io/library/mysql:5.7

[root@changgou ~]# docker images
REPOSITORY    TAG       IMAGE ID       CREATED        SIZE
hello-world   latest    d2c94e258dcb   9 months ago   13.3kB
mysql         5.7       c20987f18b13   2 years ago    448MB
mysql         latest    3218b38490ce   2 years ago    516MB
```

**docker rmi 删除镜像**

```shell
# 根据镜像id删除
[root@changgou ~]# docker rmi -f 3218b38490ce
# 删除所有镜像
[root@changgou ~]# docker rmi -f $(docker images -aq)
# 按空格分隔删除多个镜像
[root@changgou ~]# docker rmi -f 镜像id  镜像id  镜像id ...
```

# 二、容器命令

说明：有了镜像才可以创建容器, 下载一个 centos镜像来测试学习

```shell
# 准备工作
docker pull centos
```

**docker run 新建容器并启动**

```shell
# 命令格式 docker run [可选参数] imageId
# 参数说明
--name="Name" 给容器起个名字，区分容器
-d            以后台方式运行
-it           使用交互方式运行，进入容器查看内容
-p            指定容器的端口 -p 8080:8080
    -p  ip:主机端口:容器端口
    -p  主机端口：容器端口
-P            随机指定端口

# 启动并进入容器
[root@changgou ~]# docker run -it centos /bin/bash
[root@e93edc660bed /]#
# 退出容器
[root@e93edc660bed opt]# exit
exit
[root@changgou ~]# 

# exit 停止容器并退出
# ctrl + P + Q # 容器不停止退出
```

**docker ps 列出所有运行中的容器**

```shell
# 列出当前正在运行的容器
[root@changgou ~]# docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES

# 列出历史上运行过的容器
[root@changgou ~]# docker ps -a
CONTAINER ID   IMAGE          COMMAND       CREATED         STATUS                          PORTS     NAMES
e93edc660bed   centos         "/bin/bash"   2 minutes ago   Exited (0) About a minute ago             suspicious_babbage
d65bb60f2c53   d2c94e258dcb   "/hello"      26 hours ago    Exited (0) 26 hours ago                   nervous_hoover
7d7827f74dd6   d2c94e258dcb   "/hello"      26 hours ago    Exited (0) 26 hours ago                   great_antonelli

# 显示最近创建过的容器
[root@changgou ~]# docker ps -a -n=1
CONTAINER ID   IMAGE     COMMAND       CREATED         STATUS                     PORTS     NAMES
e93edc660bed   centos    "/bin/bash"   4 minutes ago   Exited (0) 2 minutes ago             suspicious_babbage

# 只显示容器的编号
[root@changgou ~]# docker ps -aq
e93edc660bed
d65bb60f2c53
7d7827f74dd6
```

**docker rm 删除容器**

```shell
docker rm 容器id                # 删除指定容器， 如果要删除正在运行的容器，需要加 -f参数 docker rm -f 容器id
docker rm -f $(docker ps -qa)  # 删除所有的容器， 或者用管道符   docker ps -qa | xargs docker rm

# 删除指定容器
[root@changgou ~]# docker ps  -a
CONTAINER ID   IMAGE          COMMAND       CREATED         STATUS                     PORTS     NAMES
e93edc660bed   centos         "/bin/bash"   8 minutes ago   Exited (0) 6 minutes ago             suspicious_babbage
d65bb60f2c53   d2c94e258dcb   "/hello"      26 hours ago    Exited (0) 26 hours ago              nervous_hoover
7d7827f74dd6   d2c94e258dcb   "/hello"      26 hours ago    Exited (0) 26 hours ago              great_antonelli
[root@changgou ~]# docker rm 7d7827f74dd6
7d7827f74dd6
[root@changgou ~]# docker ps -a
CONTAINER ID   IMAGE          COMMAND       CREATED         STATUS                     PORTS     NAMES
e93edc660bed   centos         "/bin/bash"   9 minutes ago   Exited (0) 8 minutes ago             suspicious_babbage
d65bb60f2c53   d2c94e258dcb   "/hello"      26 hours ago    Exited (0) 26 hours ago              nervous_hoover

# 删除所有容器
[root@changgou ~]# docker rm -f $(docker ps -qa) 
e93edc660bed
d65bb60f2c53
[root@changgou ~]# docker ps -a
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
```

**容器的启停操作**

```shell
docker start 容器id
docker restart 容器id
docker stop 容器id
docker kill 容器id
```

