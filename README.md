
# 1、最终一致性案例

## 1.1、选型技术

> 基于Spring Cloud Finchley.SR4、Spring Boot 2.0.6.RELEASE构建分布式环境，以下组件可以使用阿里体系替换。

- 服务注册中心
  - Netflix Eureka
- 服务网关
  - Netflix Zuul
- 客服端负载均衡
  - Netflix Ribbon
- 断路器
  - Netflix Hystrix

- 配置中心
  - 携程Apollo（本次案例未使用）

## 1.2、开发工具

- IDEA 2019
- REDIS
- RABBITMQ（创建example用户）
- 数据库（H2内嵌）

## 1.3、接口说明

- 接口文档

  - 支付接口：http://localhost:8092/swagger-ui.html

    ![image-20200417104717686](README/image-20200417104717686.png)

  - 事务消息接口：http://localhost:8090/swagger-ui.html

    ![image-20200417114356515](README/image-20200417114356515.png)

  - 短信消息接口：http://localhost:8093/swagger-ui.html

    ![image-20200417113137134](README/image-20200417113137134.png)

## 1.4、程序模块说明

- example-discovery：服务注册中心，端口8761，需要启动
- example-gateway：服务网关，端口9527，需要启动
- example-model：通用层，含实体类，工具类，统一异常处理，状态码等基本组件
- example-pay-server：支付服务，端口8092，需要启动，启动成功访问接口文档
- example-sms-server：短信服务，端口8093，需要启动，启动成功访问接口文档
- example-tx-client：提供事务远程调用接口
- example-tx-server：事务消息服务，端口8090，需要启动，启动成功访问接口文档
- example-tx-task：事务消息发送器，端口8091，需要启动，基于分布式锁实现，存在问题不支持分布式任务调度

## 1.5、接口测试说明

> 事务消息服务可以做成一个可视化界面进行管理

- 第一步：使用创建一个账户接口创建账户，此时会写入到sys_pay表中，此时不会写事务消息表sys_transaction_message。
- 第二步：使用收费接口收取费用，此时会更新sys_pay表，同时会调用远程接口写入sys_transaction_message表
- 第三步：在没有启动example-tx-task服务时，通过事务消息服务接口文档，查看写入的事务消息
- 第四步：启动事务消息发送器，等待一会儿，消息会被消费，关闭服务（开着服务会比较卡）
- 第五步：通过短信服务接口文档查看是否被写入数据

