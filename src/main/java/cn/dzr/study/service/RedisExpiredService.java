package cn.dzr.study.service;

/**
 * @Auther: dingzr
 * @Date: 2018/12/24 10:40
 * @Description:
 */
public interface RedisExpiredService {

    /**
     * 接收过期的key通知事件，进行分类处理
     *
     * @param channel 渠道
     * @param key     具体的过期key
     */
    void handelExpiredKey(String channel, String key);

    /**
     * 校验key是否过期
     *
     * @param key redis key
     */
    void deleteRedisKey(String key);


    void establishRedisListenKey();


}
