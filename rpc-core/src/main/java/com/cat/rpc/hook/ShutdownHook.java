package com.cat.rpc.hook;

import com.cat.rpc.factory.ThreadPoolFactory;
import com.cat.rpc.util.NacosUtil;
import com.cat.rpc.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownHook {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    /**
     * 在虚拟机关闭前注销所有服务以及停止线程池运行。
     */
    public void addClearAllHooks() {
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            RedisUtil.clearRegistry();
            ThreadPoolFactory.shutDownAll();
        }));
    }
}
