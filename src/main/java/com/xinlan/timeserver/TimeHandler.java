package com.xinlan.timeserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHandler extends SimpleChannelInboundHandler<String> {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " : " + msg);
        String timeStr = DATE_FORMAT.format(new Date());
        ctx.writeAndFlush(timeStr);
    }
}//end class
