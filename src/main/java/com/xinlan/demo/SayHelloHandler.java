package com.xinlan.demo;

import com.sun.tools.internal.ws.processor.model.HeaderFault;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

public class SayHelloHandler extends SimpleChannelInboundHandler<HttpRequest> {

    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {

        final String content = "Hello World!";
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1 ,
                HttpResponseStatus.OK , Unpooled.copiedBuffer(content.getBytes()));
        response.headers().add("name","panyi");
        ctx.writeAndFlush(response);
        ctx.close();
    }
}
