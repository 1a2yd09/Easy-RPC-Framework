package com.cat.rpc.transport.netty.client;

import com.cat.rpc.codec.CommonDecoder;
import com.cat.rpc.codec.CommonEncoder;
import com.cat.rpc.serializer.CommonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ChannelProvider {
    private static final Logger logger = LoggerFactory.getLogger(ChannelProvider.class);
    private static EventLoopGroup eventLoopGroup;
    private static Bootstrap bootstrap = initializeBootstrap();
    /**
     * 客户端对于每一个服务端实例和不同的序列化器仅保留一份连接，可多次复用。
     */
    private static Map<String, Channel> channels = new ConcurrentHashMap<>();

    /**
     * 根据服务端地址和序列化器代码尝试获取与服务端已建立的 channel。
     */
    public static Channel get(InetSocketAddress inetSocketAddress, CommonSerializer serializer) throws InterruptedException {
        String key = inetSocketAddress.toString() + serializer.getCode();
        if (channels.containsKey(key)) {
            Channel channel = channels.get(key);
            if (channels != null && channel.isActive()) {
                return channel;
            } else {
                channels.remove(key);
            }
        }
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                // 如果5s内没有向服务端写数据，会触发一个 IdleState#WRITER_IDLE 事件，该事件在 NettyClientHandler 中被处理:
                pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                pipeline.addLast(new CommonEncoder(serializer));
                pipeline.addLast(new CommonDecoder());
                pipeline.addLast(new NettyClientHandler());
            }
        });
        Channel channel;
        try {
            channel = connect(bootstrap, inetSocketAddress);
        } catch (ExecutionException e) {
            logger.error("连接服务端时有错误发生", e);
            return null;
        }
        channels.put(key, channel);
        return channel;
    }

    private static Channel connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                logger.info("客户端连接成功!");
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    private static Bootstrap initializeBootstrap() {
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);
        return bootstrap;
    }
}
