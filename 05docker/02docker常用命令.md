# Docker 常用命令

官方帮忙文档

https://docs.docker.com/engine/reference/commandline/docker/

# 一、帮助命令

```shell
docker version       # 显示docker的版本信息
docker info          # 显示 docker 的系统信息，包括镜像和容器的数量
docker 命令 --help    # 帮助命令
```

# 二、镜像命令

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

# 三、容器命令

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

# 四、其他常用命令

**后台启动容器**

```shell
docker run -d 容器

[root@changgou ~]# docker run -d centos
1855c11cac4dda470aa74257cb35bcdf62ebaeaae4bb5e33bfc559ba77d7a5ca
[root@changgou ~]# docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES

# docker ps ， 发现 centos 停止了
# docker 容器后台运行，就必须要有一个前台进程，docker发现没有应用，会自动停止。
```

**查看日志命令**

```shell
docker logs -f -t --tailf 条数 容器id

# 参数
 -tf               显示日志
 --tailf number    要显示的日志条数

# 测试，写一段Shell脚本
[root@changgou ~]# docker run -d centos /bin/bash -c "while true;do echo stanlong;sleep 1;done"
c6437da32a99b19af26f9ce76a4d93426d52e8057962e0dccfbd81e481acaba3

[root@changgou ~]# docker ps
CONTAINER ID   IMAGE     COMMAND                  CREATED         STATUS         PORTS     NAMES
c6437da32a99   centos    "/bin/bash -c 'while…"   4 seconds ago   Up 3 seconds             nostalgic_mendeleev

[root@changgou ~]# docker logs -tf --tail 10 c6437da32a99
2024-01-31T19:48:56.973906744Z stanlong
2024-01-31T19:48:57.977044355Z stanlong
2024-01-31T19:48:58.985026020Z stanlong
2024-01-31T19:48:59.987616494Z stanlong
2024-01-31T19:49:00.990848429Z stanlong
```

**查看容器中的进程信息**

```shell
docker top 容器id

[root@changgou ~]# docker top c6437da32a99
UID                 PID                 PPID                C                   STIME               TTY                 TIME                CMD
root                1572                1552                0                   19:48               ?                   00:00:00            /bin/bash -c while true;do echo stanlong;sleep 1;done
root                1876                1572                0                   19:52               ?                   00:00:00            /usr/bin/coreutils --coreutils-prog-shebang=sleep /usr/bin/sleep 1
```

**查看镜像元数据**

```shell
docker inspect 容器id

[root@changgou ~]# docker inspect c6437da32a99
[
    {
        "Id": "c6437da32a99b19af26f9ce76a4d93426d52e8057962e0dccfbd81e481acaba3",
        "Created": "2024-01-31T19:48:40.414781814Z",
        "Path": "/bin/bash",
        "Args": [
            "-c",
            "while true;do echo stanlong;sleep 1;done"
        ],
        "State": {
            "Status": "running",
            "Running": true,
            "Paused": false,
            "Restarting": false,
            "OOMKilled": false,
            "Dead": false,
            "Pid": 1572,
            "ExitCode": 0,
            "Error": "",
            "StartedAt": "2024-01-31T19:48:40.8808908Z",
            "FinishedAt": "0001-01-01T00:00:00Z"
        },
        "Image": "sha256:5d0da3dc976460b72c77d94c8a1ad043720b0416bfc16c52c45d4847e53fadb6",
        "ResolvConfPath": "/var/lib/docker/containers/c6437da32a99b19af26f9ce76a4d93426d52e8057962e0dccfbd81e481acaba3/resolv.conf",
        "HostnamePath": "/var/lib/docker/containers/c6437da32a99b19af26f9ce76a4d93426d52e8057962e0dccfbd81e481acaba3/hostname",
        "HostsPath": "/var/lib/docker/containers/c6437da32a99b19af26f9ce76a4d93426d52e8057962e0dccfbd81e481acaba3/hosts",
        "LogPath": "/var/lib/docker/containers/c6437da32a99b19af26f9ce76a4d93426d52e8057962e0dccfbd81e481acaba3/c6437da32a99b19af26f9ce76a4d93426d52e8057962e0dccfbd81e481acaba3-json.log",
        "Name": "/nostalgic_mendeleev",
        "RestartCount": 0,
        "Driver": "overlay2",
        "Platform": "linux",
        "MountLabel": "",
        "ProcessLabel": "",
        "AppArmorProfile": "",
        "ExecIDs": null,
        "HostConfig": {
            "Binds": null,
            "ContainerIDFile": "",
            "LogConfig": {
                "Type": "json-file",
                "Config": {}
            },
            "NetworkMode": "default",
            "PortBindings": {},
            "RestartPolicy": {
                "Name": "no",
                "MaximumRetryCount": 0
            },
            "AutoRemove": false,
            "VolumeDriver": "",
            "VolumesFrom": null,
            "ConsoleSize": [
                37,
                143
            ],
            "CapAdd": null,
            "CapDrop": null,
            "CgroupnsMode": "host",
            "Dns": [],
            "DnsOptions": [],
            "DnsSearch": [],
            "ExtraHosts": null,
            "GroupAdd": null,
            "IpcMode": "private",
            "Cgroup": "",
            "Links": null,
            "OomScoreAdj": 0,
            "PidMode": "",
            "Privileged": false,
            "PublishAllPorts": false,
            "ReadonlyRootfs": false,
            "SecurityOpt": null,
            "UTSMode": "",
            "UsernsMode": "",
            "ShmSize": 67108864,
            "Runtime": "runc",
            "Isolation": "",
            "CpuShares": 0,
            "Memory": 0,
            "NanoCpus": 0,
            "CgroupParent": "",
            "BlkioWeight": 0,
            "BlkioWeightDevice": [],
            "BlkioDeviceReadBps": [],
            "BlkioDeviceWriteBps": [],
            "BlkioDeviceReadIOps": [],
            "BlkioDeviceWriteIOps": [],
            "CpuPeriod": 0,
            "CpuQuota": 0,
            "CpuRealtimePeriod": 0,
            "CpuRealtimeRuntime": 0,
            "CpusetCpus": "",
            "CpusetMems": "",
            "Devices": [],
            "DeviceCgroupRules": null,
            "DeviceRequests": null,
            "MemoryReservation": 0,
            "MemorySwap": 0,
            "MemorySwappiness": null,
            "OomKillDisable": false,
            "PidsLimit": null,
            "Ulimits": [],
            "CpuCount": 0,
            "CpuPercent": 0,
            "IOMaximumIOps": 0,
            "IOMaximumBandwidth": 0,
            "MaskedPaths": [
                "/proc/asound",
                "/proc/acpi",
                "/proc/kcore",
                "/proc/keys",
                "/proc/latency_stats",
                "/proc/timer_list",
                "/proc/timer_stats",
                "/proc/sched_debug",
                "/proc/scsi",
                "/sys/firmware",
                "/sys/devices/virtual/powercap"
            ],
            "ReadonlyPaths": [
                "/proc/bus",
                "/proc/fs",
                "/proc/irq",
                "/proc/sys",
                "/proc/sysrq-trigger"
            ]
        },
        "GraphDriver": {
            "Data": {
                "LowerDir": "/var/lib/docker/overlay2/484b72c707bceecf5b33e1676779796793eee98019e47f3c167e13ff1a09fa56-init/diff:/var/lib/docker/overlay2/199e46fd11c18a926b3a88250e81c4d3b86e7ba8f64b15f966d5873279303845/diff",
                "MergedDir": "/var/lib/docker/overlay2/484b72c707bceecf5b33e1676779796793eee98019e47f3c167e13ff1a09fa56/merged",
                "UpperDir": "/var/lib/docker/overlay2/484b72c707bceecf5b33e1676779796793eee98019e47f3c167e13ff1a09fa56/diff",
                "WorkDir": "/var/lib/docker/overlay2/484b72c707bceecf5b33e1676779796793eee98019e47f3c167e13ff1a09fa56/work"
            },
            "Name": "overlay2"
        },
        "Mounts": [],
        "Config": {
            "Hostname": "c6437da32a99",
            "Domainname": "",
            "User": "",
            "AttachStdin": false,
            "AttachStdout": false,
            "AttachStderr": false,
            "Tty": false,
            "OpenStdin": false,
            "StdinOnce": false,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
            ],
            "Cmd": [
                "/bin/bash",
                "-c",
                "while true;do echo stanlong;sleep 1;done"
            ],
            "Image": "centos",
            "Volumes": null,
            "WorkingDir": "",
            "Entrypoint": null,
            "OnBuild": null,
            "Labels": {
                "org.label-schema.build-date": "20210915",
                "org.label-schema.license": "GPLv2",
                "org.label-schema.name": "CentOS Base Image",
                "org.label-schema.schema-version": "1.0",
                "org.label-schema.vendor": "CentOS"
            }
        },
        "NetworkSettings": {
            "Bridge": "",
            "SandboxID": "45adfd58ba62f460e1e968133250289678e7b9689828e937adaed5ac3c72d893",
            "SandboxKey": "/var/run/docker/netns/45adfd58ba62",
            "Ports": {},
            "HairpinMode": false,
            "LinkLocalIPv6Address": "",
            "LinkLocalIPv6PrefixLen": 0,
            "SecondaryIPAddresses": null,
            "SecondaryIPv6Addresses": null,
            "EndpointID": "fb0c1846e8f02e21b2b95e668ca3bb53e22e082ee84eea8528bdbf3ff15a1575",
            "Gateway": "172.17.0.1",
            "GlobalIPv6Address": "",
            "GlobalIPv6PrefixLen": 0,
            "IPAddress": "172.17.0.2",
            "IPPrefixLen": 16,
            "IPv6Gateway": "",
            "MacAddress": "02:42:ac:11:00:02",
            "Networks": {
                "bridge": {
                    "IPAMConfig": null,
                    "Links": null,
                    "Aliases": null,
                    "MacAddress": "02:42:ac:11:00:02",
                    "NetworkID": "9ed6cc559987434b88f5ad0de839cd447e916be053407b3e03f3fe187f78d5d1",
                    "EndpointID": "fb0c1846e8f02e21b2b95e668ca3bb53e22e082ee84eea8528bdbf3ff15a1575",
                    "Gateway": "172.17.0.1",
                    "IPAddress": "172.17.0.2",
                    "IPPrefixLen": 16,
                    "IPv6Gateway": "",
                    "GlobalIPv6Address": "",
                    "GlobalIPv6PrefixLen": 0,
                    "DriverOpts": null,
                    "DNSNames": null
                }
            }
        }
    }
]
```

**进入当前正在运行的容器**

```shell
# 方式一、进入容器后会开启一个新的终端
docker exec -it 容器id 

[root@changgou ~]# docker exec -it c6437da32a99 /bin/bash
[root@c6437da32a99 /]# ls
bin  dev  etc  home  lib  lib64  lost+found  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var

# 方式二、进入容器正在执行的一个终端，不会开启新的进程
docker attach 容器id

[root@changgou ~]# docker attach c6437da32a99 
stanlong
stanlong
```

**从容器内拷贝文件到主机上**

```shell
docker cp 容器id：容器内路径 目的主机路径

[root@changgou ~]# docker run -it centos /bin/bash

[root@changgou ~]# docker ps
CONTAINER ID   IMAGE     COMMAND       CREATED         STATUS         PORTS     NAMES
9a9873ed5bc0   centos    "/bin/bash"   8 seconds ago   Up 6 seconds             modest_hoover

[root@changgou sql]# docker attach 9a9873ed5bc0

[root@9a9873ed5bc0 /]# cd /home
[root@9a9873ed5bc0 home]# ls
[root@9a9873ed5bc0 home]# echo "HelloWorld" >> test.java
[root@9a9873ed5bc0 home]# exit      
exit

# 将在docker 容器中新建的test.java 拷贝到主机上
[root@changgou sql]# docker cp 9a9873ed5bc0:/home/test.java /opt/
Successfully copied 2.05kB to /opt/

[root@changgou opt]# ll
-rw-r--r-- 1 root root  11 2024-01-31 20:13:14 test.java
```

