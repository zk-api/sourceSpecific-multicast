## 模拟组播发送程序
## 1. 启动
引导类：`NettyClientApplication`,使用Spring Boot方式启动
## 2. 发送
启动后，访问http://IP:PORT/send?multicastAddr=组播地址&port=服务监听端口&flag=false

例如：
```http request
http://localhost:8081/send?multicastAddr=224.0.0.2&port=9000&flag=false
```