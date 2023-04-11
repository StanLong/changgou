# OpenResty
## OpenResty介绍
OpenResty(又称：ngx_openresty) 是一个基于 NGINX 的可伸缩的 Web 平台，由中国人章亦春发起，提供了很多高质量的第三方模块。

OpenResty 是一个强大的 Web 应用服务器，Web 开发人员可以使用 Lua 脚本语言调动 Nginx 支持的各种 C 以及 Lua 模块,更主要的是在性能方面，OpenResty可以 快速构造出足以胜任 10K 乃至1000K以上并发连接响应的超高性能 Web 应用系统。

360，UPYUN，阿里云，新浪，腾讯网，去哪儿网，酷狗音乐等都是 OpenResty 的深度用户。

OpenResty 简单理解，就相当于封装了nginx,并且集成了LUA脚本，开发人员只需要简单的其提供了模块就可以实现相关的逻辑，而不再像之前，还需要在nginx中自己编写lua的脚本，再进行调用了

## OpenResty安装
1. 添加仓库执行命令
```
[root@changgou ~]# yum install yum-utils
[root@changgou ~]# yum-config-manager --add-repo https://openresty.org/package/centos/openresty.repo
```
2. 执行安装
```
[root@changgou ~]# yum install openresty
```
3. 安装成功后，默认目录如下
```
[root@changgou openresty]# pwd
/usr/local/openresty
```
4. openresty封装了nginx，启动nginx可直接在浏览器里访问