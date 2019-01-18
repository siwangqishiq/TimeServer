package com.xinlan.heatbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import static io.netty.handler.timeout.IdleState.ALL_IDLE;

public class HeartbeatServer {

    public static void main(String[] args){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(bossGroup, workGroup);
            boot.channel(NioServerSocketChannel.class);
            boot.handler(new LoggingHandler(LogLevel.DEBUG));
            boot.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new StringDecoder());
                    pipeline.addLast(new IdleStateHandler(7,10,11));
                    pipeline.addLast(new HeartbeatHandler());
                }
            });


            ChannelFuture f = boot.bind(8899).sync();
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private static final class HeartbeatHandler extends ChannelInboundHandlerAdapter{
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(ctx.channel().remoteAddress() + " : " + msg);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if(evt instanceof IdleStateEvent){
                IdleStateEvent event = (IdleStateEvent)evt;
                switch (event.state()){
                    case ALL_IDLE:
                        System.out.println(ctx.channel().remoteAddress()+"  "+ IdleState.ALL_IDLE);
                        break;
                    case READER_IDLE:
                        System.out.println(ctx.channel().remoteAddress()+"  "+ IdleState.READER_IDLE);
                        break;
                    case WRITER_IDLE:
                        System.out.println(ctx.channel().remoteAddress()+"  "+ IdleState.WRITER_IDLE);
                        break;
                }
            }
        }
    }//end inner class

}//end class
