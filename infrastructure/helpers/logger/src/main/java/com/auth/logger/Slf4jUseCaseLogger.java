package com.auth.logger;

import com.auth.model.utils.UserCaseLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Slf4jUseCaseLogger implements UserCaseLogger {

    private static final Logger logger =  LoggerFactory.getLogger(Slf4jUseCaseLogger.class);

    @Override
    public void trace(String message) {
        logger.trace(message);
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void error(String message, Throwable t) {
        logger.error(message, t);
    }
}
