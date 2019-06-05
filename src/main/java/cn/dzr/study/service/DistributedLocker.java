package cn.dzr.study.service;

import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: dingzr
 * @Date: 2019/6/5 10:56
 * @Description:
 */
public interface DistributedLocker {

    void lock(String lockKey);

    void unlock(String lockKey);

    void lock(String lockKey, int timeout);

    void lock(String lockKey, TimeUnit unit, int timeout);

    void setRedissonClient(RedissonClient redissonClient);

}
