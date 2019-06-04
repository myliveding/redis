package cn.dzr.study.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ErrorHandler;

/**
 * @Auther: dingzr
 * @Date: 2019/1/25 10:32
 * @Description: redis监听单独的库时，需要添加异常处理
 */
public class RedisExpiredErrorHandler implements ErrorHandler {

    private static Log logger = LogFactory.getLog(RedisExpiredErrorHandler.class);

    @Override
    public void handleError(Throwable var) {
        logger.error("redis监听出现异常：" + var);
    }
}
