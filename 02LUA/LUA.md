# LUA 脚本

## 1 LUA介绍

### 1.1 lua是什么

Lua [1]  是一个小巧的[脚本语言](https://baike.baidu.com/item/%E8%84%9A%E6%9C%AC%E8%AF%AD%E8%A8%80)。它是巴西里约热内卢天主教大学（Pontifical Catholic University of Rio de Janeiro）里的一个由Roberto Ierusalimschy、Waldemar Celes 和 Luiz Henrique de Figueiredo三人所组成的研究小组于1993年开发的。 其设计目的是为了通过灵活嵌入应用程序中从而为应用程序提供灵活的扩展和定制功能。Lua由标准C编写而成，几乎在所有操作系统和平台上都可以编译，运行。Lua并没有提供强大的库，这是由它的定位决定的。所以Lua不适合作为开发独立应用程序的语言。Lua 有一个同时进行的JIT项目，提供在特定平台上的即时编译功能。

简单来说：

Lua 是一种轻量小巧的脚本语言，用标准C语言编写并以源代码形式开放， 其设计目的是为了嵌入应用程序中，从而为应用程序提供灵活的扩展和定制功能。

### 1.2 特性

- 支持面向过程(procedure-oriented)编程和函数式编程(functional programming)；
- 自动内存管理；只提供了一种通用类型的表（table），用它可以实现数组，哈希表，集合，对象；
- 语言内置模式匹配；闭包(closure)；函数也可以看做一个值；提供多线程（协同进程，并非操作系统所支持的线程）支持；
- 通过闭包和table可以很方便地支持面向对象编程所需要的一些关键机制，比如数据抽象，虚函数，继承和重载等。

### 1.3 应用场景

- 游戏开发
- 独立应用脚本
- Web 应用脚本
- 扩展和数据库插件如：MySQL Proxy 和 MySQL WorkBench
- 安全系统，如入侵检测系统
- redis中嵌套调用实现类似事务的功能
- web容器中应用处理一些过滤 缓存等等的逻辑，例如nginx。

### 1.4 LUA的安装

有linux版本的安装也有mac版本的安装。。我们采用linux版本的安装，首先我们准备一个linux虚拟机。

安装步骤,在linux系统中执行下面的命令。

```shell
# 先安装依赖库
yum install libtermcap-devel ncurses-devel libevent-devel readline-devel

# 安装LUA
wget -c  http://www.lua.org/ftp/lua-5.3.5.tar.gz
tar zxf lua-5.3.5.tar.gz
cd lua-5.3.5
make linux test

# 执行 lua 命令查看是否安装成功
[root@changgou ~]# lua
Lua 5.1.4  Copyright (C) 1994-2008 Lua.org, PUC-Rio
> 
# 或者执行 lua -a 也可以进入到 lua的控制台
[root@changgou ~]# lua -i
Lua 5.1.4  Copyright (C) 1994-2008 Lua.org, PUC-Rio
> 
```

### 1.5 入门程序

~~~shell
# 脚本编程
[root@changgou ~]# vi hello.lua
print("hello");
[root@changgou ~]# lua hello.lua 
hello
# 交互式编程
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




































