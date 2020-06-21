package com.github.zk.nettyserver.heartbeat.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 发送心跳处理器
 *
 * @author zk
 * @date 2020/6/16 15:18
 */
public class SendHeartBeatHandler extends SimpleChannelInboundHandler<String> {
    private Logger logger = LoggerFactory.getLogger(SendHeartBeatHandler.class);

    private long delay;

    public SendHeartBeatHandler(long delay) {
        this.delay = delay;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sendHeartBeat(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void sendHeartBeat(ChannelHandlerContext ctx) {
        ScheduledFuture<?> schedule = ctx.executor().parent().schedule(() -> {
            if (ctx.channel().isActive()) {
                logger.debug("send Heart Beat");
                ctx.writeAndFlush("Heart Beat");
            }
        }, delay, TimeUnit.SECONDS);
        schedule.addListener((GenericFutureListener) future -> {
            if (future.isSuccess()) {
                sendHeartBeat(ctx);
            }
        });
    }
}
