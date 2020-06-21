package com.github.zk.nettyserver.heartbeat.handler;

import com.github.zk.nettyserver.NettyServerApplication;
import com.github.zk.nettyserver.heartbeat.NettyClient;
import com.github.zk.nettyserver.server.NettyServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 心跳检测处理器
 *
 * @author zk
 * @date 2020/6/15 14:41
 */
public class HeartBeatChannelHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(HeartBeatChannelHandler.class);

    //服务接收实例
    private NettyServer nettyServer;
    //心跳启动实例
    private NettyClient nettyClient;
    //纳秒转换参数
    private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1);
    //检测时间间隔
    private long idleTimeNanos;
    //超时时间
    private ScheduledFuture<?> idleTimeout;
    //最后心跳时间
    private long lastHeartTime;

    public HeartBeatChannelHandler(long idleTimeSeconds, TimeUnit unit, NettyServer nettyServer, NettyClient nettyClient) {
        if (unit == null) {
            throw new NullPointerException("unit");
        } else {
            if (idleTimeSeconds <= 0L) {
                this.idleTimeNanos = 0L;
            } else {
                this.idleTimeNanos = Math.max(unit.toNanos(idleTimeSeconds), MIN_TIMEOUT_NANOS);
            }
        }
        this.nettyServer = nettyServer;
        this.nettyClient = nettyClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当服务为backup时，开启心跳检测
        if (!NettyServerApplication.isMaster) {
            this.initialize(ctx);
        }
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("receive : {}", msg);
        this.lastHeartTime = ticksInNanos();
    }

    private void initialize(ChannelHandlerContext ctx) {
        lastHeartTime = ticksInNanos();
        if (idleTimeNanos > 0L) {
            this.idleTimeout = this.schedule(ctx, new IdleTimeoutTask(ctx), idleTimeNanos, TimeUnit.NANOSECONDS);
        }
    }

    ScheduledFuture<?> schedule(ChannelHandlerContext ctx, Runnable task, long delay, TimeUnit unit) {
        return ctx.executor().schedule(task, delay, unit);
    }

    long ticksInNanos() {
        return System.nanoTime();
    }

    private final class IdleTimeoutTask implements Runnable {
        private ChannelHandlerContext ctx;

        public IdleTimeoutTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
        @Override
        public void run() {
            long nextDelay = idleTimeNanos;
            nextDelay -= ticksInNanos() - lastHeartTime;
            if (nextDelay <= 0) {
                logger.info("Master已死，Backup升级为Master");
                NettyServerApplication.isMaster = true;
                //超时，启动心跳
                ctx.executor().parent().execute(nettyClient::startClient);
                //启动接收服务
                ctx.executor().parent().execute(nettyServer::startNettyServer);
            } else {
                logger.debug("进入下次计时,时间间隔【" + nextDelay + "】");
               idleTimeout = schedule(ctx, this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }
}
