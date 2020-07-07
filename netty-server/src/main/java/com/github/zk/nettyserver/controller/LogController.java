package com.github.zk.nettyserver.controller;

import com.github.zk.nettyserver.enums.LogEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志动态设置 controller
 *
 * @author zk
 * @date 2020/6/4 13:44
 */
@RestController
@RequestMapping("/log")
public class LogController {
    private Logger logger = LoggerFactory.getLogger(LogController.class.getName());
    @RequestMapping("/logLevel")
    public void logLevel(@RequestParam(required = false) String loggerName, @RequestParam String logLevel) {
        LoggingSystem loggingSystem = LogbackLoggingSystem.get(null);
        String className = "";
        if (!"".equals(loggerName) && loggerName != null) {
           className = LogEnum.valueOf(loggerName).getClassName();
        }
        loggingSystem.setLogLevel(className, LogLevel.valueOf(logLevel));
    }
    @RequestMapping("/logTest")
    public void logTest() {
        logger.debug("debug....");
    }
}
