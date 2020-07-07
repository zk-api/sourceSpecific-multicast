package com.github.zk.nettyserver.enums;

/**
 * 日志控制枚举
 *
 * @author zk
 * @date 2020/7/3 16:32
 */
public enum LogEnum {
    /**
     * 接收数据信息
     */
    RECEIVE_MSG("com.github.zk.nettyserver.handler.MessageChannelHandler"),
    /**
     * 接收数量统计
     */
    RECEIVE_STATISTIC("com.github.zk.nettyserver.scheduled.RegisterScheduled");

    private final String className;

    LogEnum(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
