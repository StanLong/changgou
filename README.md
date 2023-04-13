# 畅购商城
## 第1天

- 能够说错电商的行业特点
- 能够说错电商行业的技术特点
- 理解畅购商城的技术架构
- 掌握畅购商城的工程结构
- 能实现商品的为服务搭建
- 能实现品牌的增删改查[通用Mapper+PageHelper]

## 第2天

- 掌握FastDFS的工作原理以及FastDFS的作用
- 能够实现Docker容器安装FastDFS
- 能够搭建文件上传服务器
- 实现相册管理
- 实现规格参数的管理
- 实现商品分类的管理

## 第3天

- 能够说出SPU和SKU的概念
- 能够实现新增商品，修改商品
- 能够实现商品的审核，商家下架
- 能够实现删除商品功能代码的编写
- 能够实现找回代码功能的编写
- 代码生成器

## 第4天

- 理解LUA的作用并且能编写LUA脚本
- 理解OpenResty的作用
- 能够实现广告缓存的存入与读取
- 掌握Nginx的基本配置，能使用Nginx发布网站首页
- 理解Canal的工作流程
- 能基于Canal实现缓存同步

## 第5天

- 掌握Docker环境下安装Elasticsearch
- 掌握Docker环境下给Elasticsearch配置IK分词器
- 掌握Docker下安装Kibana
- 熟练使用DSL语句操作Elasticsearch
- 实现ES导入商品搜索数据
- 实现商品分类统计搜索
- 实现多条搜索【品牌，规格条件搜索】

## 第6天

- 实现商品搜索条件筛选
- 实现商品搜索规格过滤
- 实现商品价格区间搜索
- 理解Elasticsearch权重
- 实现商品搜索分页
- 实现商品搜索排序
- 实现商品搜索高亮

## 第7天

- 理解Thymeleaf模板引擎的应用场景
- 掌握Thymeleaf常用标签
- 基于thymeleaf实现商品搜索渲染
- 实现商品搜索thymeleaf条件切换
- 实现商品详情页静态化工程搭建
- 使吸纳商品详情静态化功能实现

## 第8天

- 掌握微服务网关的系统搭建
- 理解什么是微服务网关以及它的作用
- 掌握系统中微服务的搭建
- 掌握用户密码加密存储bcrypt
- 能够说出JWT鉴权的组成
- 掌握JWT鉴权的使用
- 掌握网关使用JWT进行校验
- 掌握网关限流的实现

## 第9天

- 能够说出用户的认证流程
- 理解认证技术实现方案
- 掌握SpringSecurity Oauth2.0 入门
- 理解OAuth2.0授权模式，中的理解授权码模式和密码授权模式
- 理解公钥私钥的校验流程
- 能实现基于RSA算法生成令牌
- 能实现用户授权认证开发

## 第10天

- 实现基于OAuth+SpringSecurity权限控制
- 实现OAuth认证微服务动态加载数据
- 理解购物车实现流程
- 实现购物车页面渲染
- 实现微服务OAuth2.0认证并获取用户令牌数据
- 实现微服务与微服务之间的认证

## 第11天

- 实现OAuth登录页的配置
- 实现OAuth登录成功跳转实现
- 实现结算页查询渲染
- 实现下单操作
- 实现下的修改库存
- 实现下单增加用户积分

## 第12天

- 掌握支付实现流程
- 能够说出微信支付开发的整体思路
- 实现生成支付二维码
- 能够编写查询支付状态
- 实现支付日志的生成与订单状态的修改、删除订单
- 实现支付状态回查
- 实现MQ处理支付回调状态
- 基于定时器实现定时处理订单状态

## 第13天

- 理解本地事务、分布式事务
- 掌握CAP定理，并能说出CAP定理中的组合流程
- 理解分布式事务实现方案
- 理解常见事务模型
- 掌握RocketMQ事务消息（介绍数据最终一致）
- 理解Fescar(seata)事务模型并且能说出不同事务模型的业务
- 基于Fescar分布式事务实现下单事务控制

## 第14天

- 掌握秒杀业务实现流程
- 能够实现秒杀商品压入radis缓存
- 掌握Spring定时任务的使用
- 能够实现秒杀商品频道页展示
- 能够实现秒杀商品详情页展示
- 能够实现秒杀商品详情页倒计时
- 能够实现登录通用跳转控制
- 实现秒杀下单操作

## 第15天

- 能够说出销峰技术的解决方案
- 基于SpringBoot的异步操作实现多线程下单
- 基于Radis队列实现防止秒杀重复排队
- 基于Radis解决并发超卖问题
- 能够说出支付流程
- 实现秒杀订单支付
- 能够实现超时支付订单库存回滚

## 第16天

- 理解集群和分步式的概念
- 可以熟练搭建Eureka集群
- 理解Radis集群的原理并能搭建Radis集群
- 理解Radis哨兵策略以及哨兵策略的作用
- 掌握解决Radis击穿问题的方案
- 能够说出Radis雪崩解决的方案
- 能够搭建RabbitMQ集群
- 在项目中可以操作RabbitMQ集群