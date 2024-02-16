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
# 下载FastDFS镜像
[root@changgou ~]# docker pull morunchang/fastdfs

# 查看已安装的镜像
[root@changgou ~]# docker images
REPOSITORY           TAG       IMAGE ID       CREATED       SIZE
mysql                latest    3218b38490ce   2 years ago   516MB
morunchang/fastdfs   latest    a729ac95698a   7 years ago   460MB

# 创建docker容器
[root@changgou ~]# docker run -d --restart=always --name tracker --net=host morunchang/fastdfs sh tracker.sh
901b271007ed4d979b8352c2c5d38bc92f6481f3862ab67a7d3ba74a1240b8c0


# 创建storage容器
[root@changgou ~]# docker run -d --restart=always --name storage --net=host -e TRACKER_IP=192.168.235.20:22122 -e GROUP_NAME=group1 morunchang/fastdfs sh storage.sh
cbaf225a7cf934d8c8759596eb39a511700e0f5ffd032133caadd84826d782ae
- 使用的网络模式是 –net=host, 192.168.235.20是宿主机的IP
- group1是组名，即storage的组  
- 如果想要增加新的storage服务器，再次运行该命令，注意更换 新组名

# 查看docker进程
[root@changgou ~]# docker ps
CONTAINER ID   IMAGE                COMMAND                  CREATED          STATUS          PORTS                                                  NAMES
cbaf225a7cf9   morunchang/fastdfs   "sh storage.sh"          17 seconds ago   Up 16 seconds                                                          storage
901b271007ed   morunchang/fastdfs   "sh tracker.sh"          41 seconds ago   Up 40 seconds                                                          tracker
```

morunchang/fastdfs 已经配置好了nginx和nginx的相关插件，不需要再重新配置。

![](./doc/06.png)

查看FastDFS的nginx配置

```shell
# 进入 fastdfs 交互式命令行
[root@changgou ~]# docker exec -it storage /bin/bash

root@changgou:/# cd /etc/nginx/conf/
root@changgou:/etc/nginx/conf# ls
fastcgi.conf          fastcgi_params          koi-utf  mime.types          nginx.conf          scgi_params          uwsgi_params          win-utf
fastcgi.conf.default  fastcgi_params.default  koi-win  mime.types.default  nginx.conf.default  scgi_params.default  uwsgi_params.default

root@changgou:/etc/nginx/conf# cat nginx.conf

#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    server {
        listen       8080;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }

        location ~ /M00 {
                    root /data/fast_data/data;
                    ngx_fastdfs_module;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

}
```

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