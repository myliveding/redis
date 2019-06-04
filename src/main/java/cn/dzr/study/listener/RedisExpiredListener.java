package cn.dzr.study.listener;

import cn.dzr.study.service.RedisExpiredService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * @Auther: dingzr
 * @Date: 2018/12/21 14:03
 * @Description: redis key expired 监听器
 */
@Component
public class RedisExpiredListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RedisExpiredListener.class);

    @Autowired
    private RedisExpiredService redisExpiredService;
    private static RedisExpiredListener serverHandler;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        serverHandler = this;
        serverHandler.redisExpiredService = this.redisExpiredService;
        // 初使化时将已静态化的testService实例化
    }


    /**
     * 客户端监听订阅的topic，当有消息的时候，会触发该方法;
     * 并不能得到value, 只能得到key。
     * 姑且理解为: redis服务在key过期时通知到java服务某个key失效了, 那么在java中不可能得到这个redis-key对应的redis-value。
     * 缺陷:
     * 1: 存在多余的key; (copykey/shadowkey)
     * 2: 不严谨, 假设copykey在 12:00:00失效, 通知在12:10:00收到, 这间隔的10min内程序修改了key, 得到的并不是 失效时的value.
     * (第1点影响不大; 第2点貌似redis本身的Pub/Sub就不是严谨的, 失效后还存在value的修改, 应该在设计/逻辑上杜绝)
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {

        byte[] body = message.getBody();// 建议使用: valueSerializer
        byte[] channel = message.getChannel();
        logger.info(String.format("onMessage >> channel: %s, body: %s, bytes: %s", new String(channel), new String(body), ""));
        serverHandler.redisExpiredService.handelExpiredKey(new String(channel), new String(body));
    }


}
