package cn.dzr.study.config;

import cn.dzr.study.service.DistributedLocker;
import cn.dzr.study.service.impl.RedissonDistributedLocker;
import cn.dzr.study.util.RedissLockUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @Auther: dingzr
 * @Date: 2019/6/5 10:59
 * @Description:
 */
@Configuration
@ConditionalOnClass(Config.class)
@EnableConfigurationProperties(RedisConfig.class)
public class RedissonAutoConfiguration {

    @Autowired
    private RedisConfig redisConfig;

    /**
     * 哨兵模式自动装配
     */
//    @Bean
//    @ConditionalOnProperty(name="spring.redisson.masterName")
//    RedissonClient redissonSentinel() {
//        Config config = new Config();
//        SentinelServersConfig serverConfig = config.useSentinelServers().addSentinelAddress(redisConfig.getSentinelAddresses())
//                .setMasterName(redisConfig.getMasterName())
//                .setTimeout(redisConfig.getTimeout())
//                .setMasterConnectionPoolSize(redisConfig.getMasterConnectionPoolSize())
//                .setSlaveConnectionPoolSize(redisConfig.getSlaveConnectionPoolSize());
//
//        if(!StringUtils.isEmpty(redisConfig.getPassword())) {
//            serverConfig.setPassword(redisConfig.getPassword());
//        }
//        return Redisson.create(config);
//    }

    /**
     * 单机模式自动装配
     */
    @Bean
    RedissonClient redissonSingle() {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress("redis://" + redisConfig.getHost() + ":" + redisConfig.getPort())
                .setTimeout(redisConfig.getTimeout());

        if (!StringUtils.isEmpty(redisConfig.getPassword())) {
            serverConfig.setPassword(redisConfig.getPassword());
        }

//        serverConfig.setDatabase(2);
        return Redisson.create(config);
    }

    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     *
     * @return
     */
    @Bean
    DistributedLocker distributedLocker(RedissonClient redissonClient) {
        DistributedLocker locker = new RedissonDistributedLocker();
        locker.setRedissonClient(redissonClient);
        RedissLockUtil.setLocker(locker);
        return locker;
    }


}
