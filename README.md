# Easy-RPC-Framework

Easy-RPC-Framework 是一款基于 Nacos 实现的 RPC 框架，网络传输实现了基于 Java 原生 Socket 与 Netty 版本，并且实现了多种序列化与负载均衡算法，另外提供了基于 Redis 的注册中心版本。

## 架构

<p align="center">
    <img src="./images/RPC.png">
</p>

消费者调用提供者的方式取决于消费者的客户端选择，如选用原生 Socket 则该步调用使用 BIO，如选用 Netty 方式则该步调用使用 NIO。如该调用有返回值，则提供者向消费者发送返回值的方式同理。

## 特性

- 实现了基于 Java 原生 Socket 传输与 Netty 传输两种网络传输方式
- 实现了四种序列化算法，Json 方式、Kryo 算法、Hessian 算法与 Google Protobuf 方式（默认采用 Kryo方式序列化）
- 实现了两种负载均衡算法：随机算法与轮询算法
- 提供基于 Nacos 和 Redis 的注册中心版本，管理服务提供者信息
- 消费端如采用 Netty 方式，会复用 Channel 避免多次连接
- 如消费端和提供者都采用 Netty 方式，会采用 Netty 的心跳机制，保证连接可用
- 接口抽象良好，模块耦合度低，网络传输、序列化器、负载均衡算法可扩展
- 实现自定义的通信协议
- 服务提供侧实现服务自动注册

## 项目模块概览

- rpc-api —— 通用接口
- rpc-common —— 实体对象、工具类等公用类
- rpc-core —— 框架的核心实现
- test-client —— 测试用消费侧
- test-server —— 测试用提供侧

## 传输协议（ERF协议）

调用参数与返回值的传输采用了如下 ERF 协议（ Easy-RPC-Framework 首字母）以防止粘包：

```text
+---------------+---------------+-----------------+-------------+
|  Magic Number |  Package Type | Serializer Type | Data Length |
|    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
+---------------+---------------+-----------------+-------------+
|                          Data Bytes                           |
|                   Length: ${Data Length}                      |
+---------------------------------------------------------------+
```

|字段|解释|
| ----------- | ----------- |
|Magic Number|魔数，标识一个 ERF 协议包，0xCAFEBABE|
|Package Type|包类型，表明这是一个请求协议包还是响应协议包|
|Serializer Type|序列化器类型，表明这个包数据的序列化方式|
|Data Length|数据内容的字节长度|
|Data Bytes|传输的对象，通常是一个`RpcRequest`或`RpcResponse`对象，取决于`Package Type`字段，对象的序列化方式取决于`Serializer Type`字段|

## 使用

### 定义调用接口

```java
public interface HelloService {
    String hello(String name);
}
```

### 在服务提供侧实现该接口

```java

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello, " + name;
    }
}
```

### 编写服务提供者

```java

@ServiceScan
public class NettyTestServer {
    public static void main(String[] args) {
        RpcServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER, ServiceRegistry.NACOS_REGISTRY);
        server.start();
    }
}
```

这里选用 Netty 传输方式，序列化方式为 Google Protobuf，并指定注册中心为 Nacos。

### 在服务消费侧远程调用

```java
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.KRYO_SERIALIZER, ServiceDiscovery.NACOS_DISCOVERY, new RoundRobinLoadBalancer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String res = helloService.hello("cat");
        System.out.println(res);
    }
}
```

这里选用 Netty 的传输方式，序列化方式为 Kryo 方式，并指定负载均衡策略为轮询策略。

### 启动

以 Nacos 为注册中心为例，首先启动服务提供者，再启动服务消费者，在此之前请确保 Nacos 运行在本地 8848 端口。
