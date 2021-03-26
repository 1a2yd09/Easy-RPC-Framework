package com.cat.rpc.server;

import com.cat.rpc.entity.RpcRequest;
import com.cat.rpc.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class WorkerThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(WorkerThread.class);

    private Socket socket;
    private Object service;

    public WorkerThread(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream())) {
            // 从客户端读取一个序列化对象，这里是请求对象:
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            // 利用反射传入方法名称和参数类型获取Method对象:
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            // 调用invoke方法，传入实例和具体参数，获取返回值:
            Object returnObject = method.invoke(service, rpcRequest.getParameters());
            // 将返回值封装为RpcResponse对象发送给客户端:
            objectOutputStream.writeObject(RpcResponse.success(returnObject));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("调用或发送时有错误发生: ", e);
        }
    }
}
