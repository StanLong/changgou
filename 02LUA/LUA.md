# LUA 脚本

## 应用场景
* 游戏开发
* 独立应用脚本
* Web应用脚本
* 扩展数据库插件如 MySQL Proxy 和 MySQL WorkBench
* 安全系统，如入侵检测系统
* redis中嵌套调用实现类似事务的功能
* web容器中应用处理一些过滤 缓存等逻辑，例如nginx

## LUA的安装
1. 官网下载地址 http://www.lua.org/download.html 下载 lua-5.3.5.tar.gz
2. 上传lua到linux
3. 解压 `[root@changgou opt]# tar -zxf lua-5.3.5.tar.gz` 
4. 安装
	~~~
    安装依赖的库： yum install libtermcap-devel ncurses-devel libevent-devel readline-devel
    [root@changgou opt]# cd lua-5.3.5
    [root@changgou lua-5.3.5]# make linux test
    执行如下命令查看是否安装成功
    [root@changgou lua-5.3.5]# lua -i
	Lua 5.1.4  Copyright (C) 1994-2008 Lua.org, PUC-Rio
	>
    ~~~

## 入门程序
~~~
脚本编程
[root@changgou ~]# vi hello.lua
print("hello");
[root@changgou ~]# lua hello.lua 
hello
交互式编程
[root@changgou ~]# lua 
Lua 5.1.4  Copyright (C) 1994-2008 Lua.org, PUC-Rio
> print("Hello lua");
Hello lua
>
~~~

## 基本语法
1. 单行注释 `--`
2. 多行注释
```
--[[
多行注释
多行注释
--]]
```
3. 定义变量
```
-- 全局变量赋值
a=1
-- 局部变量赋值
local b=2
[root@changgou ~]# lua -i
Lua 5.1.4  Copyright (C) 1994-2008 Lua.org, PUC-Rio
> a=1
> print(a)
1
> local b=2
> print(b)
nil
> local b=2;print(b)
2
>
```
4. 数据类型
| 数据类型 | 描述 |
|--------|--------|
|     nil   |  这个最简单，只有值nil属于该类，表示一个无效值（在条件表达式中相当于false）      |
|     boolean   |  包含两个值：false和true。      |
|     number   |  表示双精度类型的实浮点数      |
|     string   |  字符串由一对双引号或单引号来表示      |
|     function   |  由 C 或 Lua 编写的函数      |
|     userdata   |  表示任意存储在变量中的C数据结构      |
|     thread   |  表示执行的独立线路，用于执行协同程序      |
|     table   |  Lua 中的表（table）其实是一个"关联数组"（associative arrays），数组的索引可以是数字、字符串或表类型。在 Lua 里，table 的创建是通过"构造表达式"来完成，最简单构造表达式是{}，用来创建一个空表。      |
5. 流程控制
	如下：类似于if else
```
--[ 0 为 true ]
if(0) then
    print("0 为 true")
else
    print("0 不为true")
end
> age=16
> if(age<18)
>> then
>> print("未成年")
>> else
>> print("已成年")
>> end
未成年
```
6. 函数
```
--[[ 函数返回两个值的最大值 --]]
function max(num1, num2)
   if (num1 > num2) then
      result = num1;
   else
      result = num2;
   end
   return result;
end
-- 调用函数
print("两值比较最大值为 ",max(10,4))
print("两值比较最大值为 ",max(5,6))
```
7. 模块
require 用于 引入其他的模块，类似于java中的类要引用别的类的效果
+ 定义模块文件
```
[root@changgou ~]# vi module.lua
--定义一个模块
--定义一个模块
module={}
--给该模块定义一个变量
module.username="张三"
--定义一个全局方法
function module.fun1()
        print("fun1")
end
--定义一个局部方法
local function fun2()
        print("fun2")
end
--定义一个全局的方法，调用fun2
function module.fun3()
        fun2()
end
return
```
+ 引入模块文件
```
[root@changgou ~]# vi demo.lua
--引入module模块
require("module")
--输出module的参数
print(module.username)
--调用module中的方法
module.fun1()
module.fun3()
```
+ 执行lua脚本
```
[root@changgou ~]# lua demo.lua
张三
fun1
fun2
```

# 畅购商城项目中用到的lua脚本解读
## update_content.lua
```
ngx.header.content_type="application/json;charset=utf8"
local cjson = require("cjson")
local mysql = require("resty.mysql")
local uri_args = ngx.req.get_uri_args() # 获取用户请求的参数
local id = uri_args["id"] # 获取请求参数中的position的值
local db = mysql:new() # 连接数据库
db:set_timeout(1000)
local props = {
    host = "192.168.235.21",
    port = 3306,
    database = "changgou_content",
    user = "root",
    password = "root"
}
local res = db:connect(props) # 获取连接
local select_sql = "select url,pic from tb_content where status ='1' and category_id='"..id.."' order by sort_order"
res = db:query(select_sql) # 执行sql语句
db:close()
-- redis的相关操作
local redis = require("resty.redis")
local red = redis:new()
red:set_timeout(2000)
local ip ="192.168.235.21"
local port = 6379
red:connect(ip,port)
red:set("content_"..id,cjson.encode(res)) # 把mysql中查到的数据转成 json 格式保存到redis中
red:close()
ngx.say("{flag:true}")

```
## read_content.lua
```
ngx.header.content_type="application/json;charset=utf8"
local uri_args = ngx.req.get_uri_args();
local id = uri_args["id"];
--获取本地缓存
local cache_ngx = ngx.shared.dis_cache;
--根据ID获取本地缓存数据 
local contentCache = cache_ngx:get('content_cache_'..id);
ngx.say(contentCache) 

if contentCache == "" or contentCache == nil then
    local redis = require("resty.redis");
    local red = redis:new()
    red:set_timeout(2000)
	red:connect("192.168.235.21", 6379)
	local rescontent = red:get("content_"..id);

	if ngx.null == rescontent then
		local cjson = require("cjson");
		local mysql = require("resty.mysql");
		local db = mysql:new();
		db:set_timeout(2000)
		local props = {
			host = "192.168.235.21",
			port = 3306,
			database = "changgou_content",
			user = "root",
			password = "root"
		}
		local res = db:connect(props);
		local select_sql = "select url,pic from tb_content where status ='1' and category_id='"..id.."' order by sort_order"
		res = db:query(select_sql)
		local responsejson = cjson.encode(res);
		red:set("content_"..id,responsejson);
		ngx.say(responsejson);
		db:close()
	else
		cache_ngx:set('content_cache_'..id, rescontent, 2*60);
		ngx.say(rescontent)
	end	
    red:close()
else
    ngx.say(contentCache)
end

```




































