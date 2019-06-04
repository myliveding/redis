package cn.dzr.study.service.impl;

import cn.dzr.study.service.RedisExpiredService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: dingzr
 * @Date: 2018/12/24 10:38
 * @Description: redis key 过期处理接口
 */

@Service
@Slf4j
@Transactional
@SuppressWarnings("unchecked")
public class RedisExpiredServiceImpl implements RedisExpiredService {


    @Resource(name = "redisTemplateForListen")
    private RedisTemplate redisTemplateForListen;


    /**
     * 需要做过期key回调处理key的开始字符串
     */
    private static final String KEY_EVENT_EXPIRED_KEY_START_STRING = "xbz_service:expired_listen:";


    /**
     * 接收过期的key通知事件，进行分类处理
     *
     * @param channel 渠道
     * @param key     具体的过期key
     */
    @Override
    public void handelExpiredKey(String channel, String key) {
        log.info("业务中接收到的 channel：" + channel + " -- body：" + key);

    }


    /**
     * 校验key是否过期
     * 删除不需要的redis key
     */
    @Override
    @SuppressWarnings("all")
    public void deleteRedisKey(String key) {
        try {
            if (redisTemplateForListen.hasKey(key)) {
                redisTemplateForListen.delete(key);
            }
        } catch (Exception e) {
            log.error("删除redis key：" + key + "失败" + e.getMessage());
        }
    }

    /**
     * 创建邀约过期的倒计时redis key
     */
    public void establishRedisListenKey() {
        redisTemplateForListen.opsForValue().set(KEY_EVENT_EXPIRED_KEY_START_STRING + "_" + 1, 7200, 10, TimeUnit.SECONDS);
    }

}
