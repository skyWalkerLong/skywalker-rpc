package com.skywalker.rpc.server.bootstrap;

import com.skywalker.rpc.registry.Register;
import com.skywalker.rpc.server.annotation.RpcService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * @author longchao
 * @date 2018/8/17.
 */
public class RpcServer implements ApplicationContextAware, InitializingBean{

    private static final Logger log = LoggerFactory.getLogger(RpcServer.class);

    /**
     * 服务端地址
     */
    private String serverAddress;

    /**
     * 注册器
     */
    private Register register;

    /**
     * 存放注册的接口名及实例
     */
    private Map<String,Object> handlerMap = new HashMap<>();

    public RpcServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public RpcServer(String serverAddress, Register register) {
        this.serverAddress = serverAddress;
        this.register = register;
    }

    /**
     * 将所有使用rpc注解的接口及实现对象存到handlerMap中
     * 该方法在RpcServer注册Bean时自动调用
     * @param ctx
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        if (serviceBeanMap != null && serviceBeanMap.size() != 0) {
            serviceBeanMap.values().forEach(serviceBean -> {
                handlerMap.put(serviceBean.getClass().getAnnotation(RpcService.class).value().getName(),serviceBean);
            });
        }
    }

    /**
     * 启动netty，服务器地址注册到第三方平台
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast()
                                    .addLast()
                                    .addLast();
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            ChannelFuture future = bootstrap.bind(host, port).sync();

            log.debug("server started ! ip:{} ,port:{}",host,port);

            if (register != null) {
                register.register(serverAddress);
            }

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

}
