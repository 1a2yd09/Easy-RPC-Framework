package com.cat.rpc.client;

import com.cat.rpc.entity.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    public Object sendRequest(RpcRequest rpcRequest, String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            // 开启一个socket，获取输入流和输出流，输入流就是从服务端读取数据，输出流就是向服务端传输数据:
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 将请求对象写入输出流既发送序列化对象到服务端:
            objectOutputStream.writeObject(rpcRequest);
            // 防止缓存:
            objectOutputStream.flush();
            // 从服务端读取一个序列化对象实例:
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用时有错误发生: ", e);
            return null;
        }
    }
}
