package com.xinlan.heatbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.util.Scanner;

public class BeatClient {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap strap = new Bootstrap();
            strap.group(group);
            strap.channel(NioSocketChannel.class);
            strap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipline = socketChannel.pipeline();
                    pipline.addLast("logger", new LoggingHandler());
                    pipline.addLast(new StringEncoder());
                    pipline.addLast(new StringDecoder());
                    pipline.addLast(new CustomHandler());
                }
            });
            ChannelFuture future = strap.connect("127.0.0.1", 8899).sync();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("input command : ");
                String cmd = scanner.next();
                if ("exit".equals(cmd)) {
                    future.channel().close();
                    break;
                }
                future.channel().writeAndFlush(cmd);
            }//end while
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    /**
     *
     */
    private static final class CustomHandler extends SimpleChannelInboundHandler<String> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        }
    }//end inner class

}//end class
