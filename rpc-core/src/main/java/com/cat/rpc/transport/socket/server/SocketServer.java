package com.cat.rpc.transport.socket.server;

import com.cat.rpc.enumeration.RpcError;
import com.cat.rpc.exception.RpcException;
import com.cat.rpc.factory.ThreadPoolFactory;
import com.cat.rpc.handler.RequestHandler;
import com.cat.rpc.hook.ShutdownHook;
import com.cat.rpc.provider.ServiceProvider;
import com.cat.rpc.provider.ServiceProviderImpl;
import com.cat.rpc.registry.NacosServiceRegistry;
import com.cat.rpc.registry.ServiceRegistry;
import com.cat.rpc.serializer.CommonSerializer;
import com.cat.rpc.transport.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class SocketServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private final String host;
    private final int port;
    private final CommonSerializer serializer;
    private final ExecutorService threadPool;
    private final RequestHandler requestHandler = new RequestHandler();

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public SocketServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        this.serializer = CommonSerializer.getByCode(serializer);
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
    }

    @Override
    public <T> void publishService(T service, Class<T> serviceClass) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        // 向本地注册表注册服务:
        serviceProvider.addServiceProvider(service, serviceClass);
        // 向注册中心注册服务:
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        this.start();
    }

    @Override
    public void start() {
        ShutdownHook.getShutdownHook().addClearAllHooks();
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            logger.info("服务端启动...");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                logger.info("客户端连接: {}:{}", socket.getInetAddress(), socket.getPort());
                // 向线程池中提交新的 Socket 连接处理任务:
                threadPool.execute(new SocketRequestHandlerThread(socket, requestHandler, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("服务器启动时有错误发生:", e);
        }
    }
}
