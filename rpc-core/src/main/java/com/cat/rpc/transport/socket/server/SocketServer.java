package com.cat.rpc.transport.socket.server;

import com.cat.rpc.factory.ThreadPoolFactory;
import com.cat.rpc.handler.RequestHandler;
import com.cat.rpc.hook.ShutdownHook;
import com.cat.rpc.provider.ServiceProviderImpl;
import com.cat.rpc.registry.ServiceRegistry;
import com.cat.rpc.serializer.CommonSerializer;
import com.cat.rpc.transport.AbstractRpcServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class SocketServer extends AbstractRpcServer {
    private final CommonSerializer serializer;
    private final ExecutorService threadPool;
    private final RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER, DEFAULT_REGISTRY);
    }

    public SocketServer(String host, int port, Integer serializer, Integer registry) {
        this.host = host;
        this.port = port;
        this.serializer = CommonSerializer.getByCode(serializer);
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = ServiceRegistry.getByCode(registry);
        this.serviceProvider = new ServiceProviderImpl();
        scanServices();
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
