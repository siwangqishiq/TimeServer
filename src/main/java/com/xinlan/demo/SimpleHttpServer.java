package com.xinlan.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class SimpleHttpServer {
    public static final int PORT = 9999;

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootStrap = new ServerBootstrap();
            serverBootStrap.group(bossGroup , workerGroup);
            serverBootStrap.channel(NioServerSocketChannel.class);
            serverBootStrap.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast("codec" , new HttpServerCodec());
                    socketChannel.pipeline().addLast("sayHello" ,new SayHelloHandler());
                }
            });
            ChannelFuture future = serverBootStrap.bind(PORT);

            future.sync();
            future.channel().closeFuture().sync();
            //future.channel().closeFuture();
        }finally {

            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}//end class
