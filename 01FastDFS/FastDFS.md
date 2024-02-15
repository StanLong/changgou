# FastDFS

## 1 FastDFS

### 1.1 FastDFS简介

#### 1.1.1 FastDFS体系结构 

FastDFS是一个开源的轻量级分布式文件系统，它对文件进行管理，功能包括：文件存储、文件同步、文件访问（文件上传、文件下载）等，解决了大容量存储和负载均衡的问题。特别适合以文件为载体的在线服务，如相册网站、视频网站等等。

FastDFS为互联网量身定制，充分考虑了冗余备份、负载均衡、线性扩容等机制，并注重高可用、高性能等指标，使用FastDFS很容易搭建一套高性能的文件服务器集群提供文件上传、下载等服务。

FastDFS 架构包括 Tracker server 和 Storage server。客户端请求 Tracker server 进行文件上传、下载，通过Tracker server 调度最终由 Storage server 完成文件上传和下载。

Tracker server 作用是负载均衡和调度，通过 Tracker server 在文件上传时可以根据一些策略找到Storage server 提供文件上传服务。可以将 tracker 称为追踪服务器或调度服务器。Storage server 作用是文件存储，客户端上传的文件最终存储在 Storage 服务器上，Storageserver 没有实现自己的文件系统而是利用操作系统的文件系统来管理文件。可以将storage称为存储服务器。

![](./doc/01.png)



#### 1.1.2 上传流程

![](./doc/02.png)

客户端上传文件后存储服务器将文件 ID 返回给客户端，此文件 ID 用于以后访问该文件的索引信息。文件索引信息包括：组名，虚拟磁盘路径，数据两级目录，文件名。

![](./doc/03.png)

**组名**：文件上传后所在的 storage 组名称，在文件上传成功后有storage 服务器返回，需要客户端自行保存。

**虚拟磁盘路径**：storage 配置的虚拟路径，与磁盘选项store_path*对应。如果配置了store_path0 则是 M00，如果配置了 store_path1 则是 M01，以此类推。

**数据两级目录**：storage 服务器在每个虚拟磁盘路径下创建的两级目录，用于存储数据文件。

**文件名**：与文件上传时不同。是由存储服务器根据特定信息生成，文件名包含：源存储服务器 IP 地址、文件创建时间戳、文件大小、随机数和文件拓展名等信息。

## 2 FastDFS安装

1. 上传如下版本包到linux
	fastdfs-nginx-module_v1.16.tar.gz
	FastDFS_v5.05.tar.gz
	libfastcommonV1.0.7.tar.gz
	nginx-1.12.2.tar.gz
2. 安装libfastcommonV1.0.7.tar.gz
	~~~
    yum仓库安装相关依赖
    gcc-g++, pcre-devel, zlib-devel, openssl, openssl-devel, perl-devel

    [root@changgou libfastcommon-1.0.7]# tar -zxf libfastcommonV1.0.7.tar.gz
    [root@changgou libfastcommon-1.0.7]# cd libfastcommon-1.0.7/
    [root@changgou libfastcommon-1.0.7]# ./make.sh
    [root@changgou libfastcommon-1.0.7]# ./make.sh install
    mkdir -p /usr/lib64
    install -m 755 libfastcommon.so /usr/lib64
    mkdir -p /usr/include/fastcommon
    install -m 644 common_define.h hash.h chain.h logger.h base64.h shared_func.h pthread_func.h ini_file_reader.h _os_bits.h sockopt.h sched_thread.h http_func.h md5.h local_ip_func.h avl_tree.h ioevent.h ioevent_loop.h fast_task_queue.h fast_timer.h process_ctrl.h fast_mblock.h connection_pool.h /usr/include/fastcommon

    将 libfastcommon.so 从 /usr/lib64 移动到 /usr/lib 目录下
    [root@changgou libfastcommon-1.0.7]# cp /usr/lib64/libfastcommon.so /usr/lib/
   ~~~
3. 安装 FastDFS_v5.05.tar.gz
	~~~
    [root@changgou FastDFS]# tar -zxf FastDFS_v5.05.tar.gz
    [root@changgou FastDFS]# cd FastDFS
    [root@changgou FastDFS]# ./make.sh
    [root@changgou FastDFS]# ./make.sh install
    mkdir -p /usr/bin
    mkdir -p /etc/fdfs
    cp -f fdfs_trackerd /usr/bin
    if [ ! -f /etc/fdfs/tracker.conf.sample ]; then cp -f ../conf/tracker.conf /etc/fdfs/tracker.conf.sample; fi
    mkdir -p /usr/bin
    mkdir -p /etc/fdfs
    cp -f fdfs_storaged  /usr/bin
    if [ ! -f /etc/fdfs/storage.conf.sample ]; then cp -f ../conf/storage.conf /etc/fdfs/storage.conf.sample; fi
    mkdir -p /usr/bin
    mkdir -p /etc/fdfs
    mkdir -p /usr/lib64
    cp -f fdfs_monitor fdfs_test fdfs_test1 fdfs_crc32 fdfs_upload_file fdfs_download_file fdfs_delete_file fdfs_file_info fdfs_appender_test fdfs_appender_test1 fdfs_append_file fdfs_upload_appender /usr/bin
    if [ 0 -eq 1 ]; then cp -f libfdfsclient.a /usr/lib64; fi
    if [ 1 -eq 1 ]; then cp -f libfdfsclient.so /usr/lib64; fi
    mkdir -p /usr/include/fastdfs
    cp -f ../common/fdfs_define.h ../common/fdfs_global.h ../common/mime_file_parser.h ../common/fdfs_http_shared.h ../tracker/tracker_types.h ../tracker/tracker_proto.h ../tracker/fdfs_shared_func.h ../storage/trunk_mgr/trunk_shared.h tracker_client.h storage_client.h storage_client1.h client_func.h client_global.h fdfs_client.h /usr/include/fastdfs
    if [ ! -f /etc/fdfs/client.conf.sample ]; then cp -f ../conf/client.conf /etc/fdfs/client.conf.sample; fi

    安装完成之后可以看到
    [root@changgou FastDFS]# cd /etc/fdfs/
    [root@changgou fdfs]# ll
    total 20
    -rw-r--r-- 1 root root 1461 Feb  8 17:50 client.conf.sample
    -rw-r--r-- 1 root root 7829 Feb  8 17:50 storage.conf.sample
    -rw-r--r-- 1 root root 7102 Feb  8 17:50 tracker.conf.sample

    # 拷贝文件
    [root@changgou conf]# pwd
    /opt/fastdfs/FastDFS/conf
    [root@changgou conf]# ll
    total 84
    -rw-r--r-- 1 8980 users 23981 Dec  2  2014 anti-steal.jpg
    -rw-r--r-- 1 8980 users  1461 Dec  2  2014 client.conf
    -rw-r--r-- 1 8980 users   858 Dec  2  2014 http.conf
    -rw-r--r-- 1 8980 users 31172 Dec  2  2014 mime.types
    -rw-r--r-- 1 8980 users  7829 Dec  2  2014 storage.conf
    -rw-r--r-- 1 8980 users   105 Dec  2  2014 storage_ids.conf
    -rw-r--r-- 1 8980 users  7102 Dec  2  2014 tracker.conf
    [root@changgou conf]# cp * /etc/fdfs/
   ~~~
4. 安装tracker服务
	~~~
    [root@changgou fdfs]# cd /etc/fdfs/
    [root@changgou fdfs]# vi tracker.conf
    21 # the base path to store data and log files
    22 base_path=/opt/fastdfs/FastDFS/tracker

    启动tracker
    [root@changgou fdfs]# /usr/bin/fdfs_trackerd /etc/fdfs/tracker.conf
   ~~~
5. 安装storage服务
	~~~
    [root@changgou fdfs]# cd /etc/fdfs/
    [root@changgou fdfs]# vi storage.conf
    40 # the base path to store data and log files
    41 base_path=/opt/fastdfs/FastDFS/storage
    #########################
    107 # store_path#, based 0, if store_path0 not exists, it's value is base_path
    108 # the paths must be exist
    109 store_path0=/opt/fastdfs/FastDFS/storage
    110 #store_path1=/home/yuqing/fastdfs2
    #########################
    116 # tracker_server can ocur more than once, and tracker_server format is
    117 #  "host:port", host can be hostname or ip address
    118 tracker_server=192.168.235.21:22122

    启动storage
    [root@changgou fdfs]# /usr/bin/fdfs_storaged /etc/fdfs/storage.conf
   ~~~
6. 配置客户端
	+ 将 /opt/fastdfs/FastDFS/client/libfdfsclient.so 拷贝到 /usr/lib 目录下
	+ 修改配置文件 /etc/fdfs/client.conf
	~~~
    [root@changgou client]# vi /etc/fdfs/client.conf
    9 # the base path to store log files
    10 base_path=/opt/fastdfs/FastDFS/client
    #########################
    12 # tracker_server can ocur more than once, and tracker_server format is
    13 #  "host:port", host can be hostname or ip address
    14 tracker_server=192.168.235.21:22122
   ~~~
7. 上传文件测试
	~~~
    [root@changgou ~]# /usr/bin/fdfs_test /etc/fdfs/client.conf upload userAdd.sh 
    [2020-02-08 18:25:41] DEBUG - base_path=/opt/fastdfs/FastDFS/client, connect_timeout=30, network_timeout=60, tracker_server_count=1, anti_steal_token=0, anti_steal_secret_key length=0, use_connection_pool=0, g_connection_pool_max_idle_time=3600s, use_storage_id=0, storage server id count: 0
    tracker_query_storage_store_list_without_group: 
        server 1. group_name=, ip_addr=192.168.235.21, port=23000
    group_name=group1, ip_addr=192.168.235.21, port=23000
    storage_upload_by_filename
    group_name=group1, remote_filename=M00/00/00/wKjrFV4-jKWAYq-dAAABAY1EKd83951.sh
    source ip address: 192.168.235.21
    file timestamp=2020-02-08 18:25:41
    file size=257
    file crc32=2370054623
    example file url: http://192.168.235.21/group1/M00/00/00/wKjrFV4-jKWAYq-dAAABAY1EKd83951.sh
    storage_upload_slave_by_filename
    group_name=group1, remote_filename=M00/00/00/wKjrFV4-jKWAYq-dAAABAY1EKd83951_big.sh
    source ip address: 192.168.235.21
    file timestamp=2020-02-08 18:25:41
    file size=257
    file crc32=2370054623
    example file url: http://192.168.235.21/group1/M00/00/00/wKjrFV4-jKWAYq-dAAABAY1EKd83951_big.sh

    查看文件
    [root@changgou 00]# cd /opt/fastdfs/FastDFS/storage/data/00/00
    [root@changgou 00]# ll
    total 16
    -rw-r--r-- 1 root root 257 Feb  8 18:25 wKjrFV4-jKWAYq-dAAABAY1EKd83951_big.sh
    -rw-r--r-- 1 root root  49 Feb  8 18:25 wKjrFV4-jKWAYq-dAAABAY1EKd83951_big.sh-m
    -rw-r--r-- 1 root root 257 Feb  8 18:25 wKjrFV4-jKWAYq-dAAABAY1EKd83951.sh
    -rw-r--r-- 1 root root  49 Feb  8 18:25 wKjrFV4-jKWAYq-dAAABAY1EKd83951.sh-m
   ~~~

# nginx及nginx插件的安装
## 插件安装
~~~
[root@changgou fastdfs]# tar -zxf fastdfs-nginx-module_v1.16.tar.gz
[root@changgou fastdfs]# cd fastdfs-nginx-module
~~~
2. 修改src/config文件
~~~
[root@changgou fastdfs-nginx-module]# vi src/config 
ngx_addon_name=ngx_http_fastdfs_module
HTTP_MODULES="$HTTP_MODULES ngx_http_fastdfs_module"
NGX_ADDON_SRCS="$NGX_ADDON_SRCS $ngx_addon_dir/ngx_http_fastdfs_module.c"
CORE_INCS="$CORE_INCS /usr/local/include/fastdfs /usr/local/include/fastcommon/"
CORE_LIBS="$CORE_LIBS -L/usr/local/lib -lfastcommon -lfdfsclient"
CFLAGS="$CFLAGS -D_FILE_OFFSET_BITS=64 -DFDFS_OUTPUT_CHUNK_SIZE='256*1024' -DFDFS_MOD_CONF_FILENAME='\"/etc/fdfs/mod_fastdfs.conf\"'"
:%s/local\///g -全局替换命令， 把 local/ 替换成空
~~~
3. 复制src/mod_fastdfs.conf文件到/etc/fdfs目录下，并编辑
~~~
[root@changgou src]# cp mod_fastdfs.conf /etc/fdfs/
[root@changgou src]# cd /etc/fdfs/
[root@changgou fdfs]# vi mod_fastdfs.conf
37 # FastDFS tracker_server can ocur more than once, and tracker_server format is
38 #  "host:port", host can be hostname or ip address
39 # valid only when load_fdfs_parameters_from_tracker is true
40 tracker_server=192.168.235.21:22122
#########################
49 # if the url / uri including the group name
50 # set to false when uri like /M00/00/00/xxx
51 # set to true when uri like ${group_name}/M00/00/00/xxx, such as group1/M00/xxx
52 # default value is false
53 url_have_group_name = true 	#url地址包含组名称
#########################
59 # store_path#, based 0, if store_path0 not exists, it's value is base_path
60 # the paths must be exist
61 # must same as storage.conf
62 store_path0=/opt/fastdfs/FastDFS/storage
63 #store_path1=/home/yuqing/fastdfs1
~~~

## nginx安装（参考04编译安装）

1. 04安装nginx的时候没有加入fdfs模块，这里需要重新加入nginx模板
~~~
[root@changgou sbin]# ./nginx -V # 查看已支持的模块（没有fdfs的）
进入 nginx安装目录，删除Makefile文件
然后执行如下命令
[root@changgou tengine-2.1.0]# ./configure --prefix=/opt/tengine-2.1.0/nginx --add-module=/opt/fdfs/fastdfs-nginx-module/src/
# --add-module=/opt/fdfs/fastdfs-nginx-module/src/ 为添加 fdfs模块
执行成功后，再执行 ./nginx -V 命令
[root@changgou sbin]# ./nginx -V
Tengine version: Tengine/2.1.0 (nginx/1.6.2)
built by gcc 4.8.5 20150623 (Red Hat 4.8.5-39) (GCC) 
TLS SNI support enabled
configure arguments: --prefix=/opt/tengine-2.1.0/nginx --add-module=/opt/fdfs/fastdfs-nginx-module/src/
~~~

2. 在nginx.conf文件中配置插件信息
~~~
[root@changgou conf]# cd /opt/tengine-2.1.0/nginx/conf
47         #access_log  logs/host.access.log  main;
48         location /group1/M00/{	#请求路径中包含/group1/M00/转由 ngx_fastdfs_module 处理
49             ngx_fastdfs_module;
50         }
~~~

3. 启动nginx
~~~
[root@changgou sbin]# ./nginx
ngx_http_fastdfs_set pid=6311
~~~

4. 写个html文件测试下
~~~
[root@changgou ~]# vi testFdfs.html
<h1>Hello HDFS</h1>
上传 testFdfs.html 到fdfs
[root@changgou ~]# /usr/bin/fdfs_test /etc/fdfs/client.conf upload testFdfs.html
example file url: http://192.168.235.21/group1/M00/00/00/wKjrFV4_vDuAGCOSAAAAFInvfFg85_big.html
访问 http://192.168.235.21/group1/M00/00/00/wKjrFV4_vDuAGCOSAAAAFInvfFg85_big.html 成功
~~~
