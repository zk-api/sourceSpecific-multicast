# 更新说明
1. 优化转发方式
2. 调整寻找备机时间间隔，兼容Linux系统
# 使用说明
## 1. 功能介绍
- 基于Netty对源特定组播数据接收，可以对组播源、网卡、源地址、端口互相组合接收。
并且采用端口复用，可以监听多个相同端口。
- 支持接收数据量统计：分为每秒接收量和从启动开始接收的数据总量两个维度.
浏览器访问：`localhost:8080/log/logLevel?logLevel=DEBUG`,即可在后台日志种查看到统计值
- 将接收到的源特定组播数据转发为普通组播或点对点单播
## 2. 单点配置
```yaml
netty:
  zk:
    #数据接收配置
    hosts:
        #组播地址
      - multicast: 232.2.33.1
        #网卡地址
        network: 172.20.160.158
        #源地址
        sources: 172.20.160.158
        #监听端口
        ports: 9000
      - multicast: 224.0.0.2
        network: 172.20.160.158
        sources: 172.20.160.158
        ports: 9001,9002
      - multicast: 224.0.0.2
        network: 172.20.160.158
        sources: 172.20.160.158
        ports: 9001,9002
spring:
  task:
    scheduling:
      pool:
        size: 2
```
多个组播地址用“，”隔开，多个源地址用“，”隔开，多个端口用“，”隔开。
## 3. 主备配置
```yaml
netty:
  zk:
    hosts:
      - multicast: 232.2.33.1
        network: 172.20.160.158
        sources: 172.20.160.158
        ports: 9000
      - multicast: 224.0.0.2
        network: 172.20.160.158
        sources: 172.20.160.158
        ports: 9001,9002
      - multicast: 224.0.0.2
        network: 172.20.160.158
        sources: 172.20.160.158
        ports: 9001,9002
    # 发送心跳地址（备机心跳检测地址）
    sendHeartBeat:
      host: 127.0.0.1
      port: 10001
    # 心跳检测端口
    checkHeartBeat:
      port: 10000
# 定时统计接收量相关配置
spring:
  task:
    scheduling:
      pool:
        size: 2
```
- netty.zk.sendHeartBeat.host： 备机IP
- netty.zk.sendHeartBeat.port： 备机心跳检测端口
- netty.zk.checkHeartBeat.port：心跳检测端口
## 4. 转发配置
只需要配置好转发IP和端口即可进行转发
```yaml
netty:
    zk:
      # 转发地址
      forward:
        host: 224.0.0.1
        port: 9000
```
## 5. 程序启动
引导类：`NettyServerApplication` ，使用Spring Boot方式启动。
## 6. 打包
```shell script
mvn clean package -Dmaven.test.skip=true
```
## 7. 动态调整日志级别
如果想查看日志，程序启动后，浏览器访问
```http request
localhost:8080/log/logLevel?logLevel=DEBUG
```
> 注意ip和port使用自己配置的

访问后可看到控制台输出心跳检测、统计接收量等日志
> 服务重启需要重新访问链接