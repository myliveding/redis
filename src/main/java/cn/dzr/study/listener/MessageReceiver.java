package cn.dzr.study.listener;

import cn.dzr.study.service.RedisExpiredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: dingzr
 * @Date: 2019/1/25 10:57
 * @Description: 处理消息的方法
 */
@Component
public class MessageReceiver {

    private final RedisExpiredService redisExpiredService;

    @Autowired
    public MessageReceiver(RedisExpiredService redisExpiredService) {
        this.redisExpiredService = redisExpiredService;
    }

    /**
     * 接收消息的方法
     */
    public void receiveMessage(String message) {
        redisExpiredService.handelExpiredKey(message, "key值信息");
    }
}
