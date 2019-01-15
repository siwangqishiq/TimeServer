package com.xinlan.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HelloNettyHandler extends SimpleChannelInboundHandler<HttpObject> {
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
        ByteBuf content = Unpooled.copiedBuffer("<h1>Hello Netty</h1>" , CharsetUtil.UTF_8);
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1 , HttpResponseStatus.OK , content);
        response.headers().set("Content-Length" , content.readableBytes());
        ctx.writeAndFlush(response);

    }
}//end class
