package cn.dzr.study.config;

import cn.dzr.study.service.DistributedLocker;
import cn.dzr.study.service.impl.RedissonDistributedLocker;
import cn.dzr.study.util.RedissLockUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
     * 单机模式自动装配
     */
    @Bean(destroyMethod = "shutdown")
    RedissonClient redissonCluster() {
        Config config = new Config();
        ClusterServersConfig serverConfig = config.useClusterServers()
                .setScanInterval(2000) // 集群状态扫描间隔时间，单位是毫秒
                //可以用"rediss://"来启用SSL连接
                .addNodeAddress(redisConfig.getNodes());
        if (!StringUtils.isEmpty(redisConfig.getPassword())) {
            serverConfig.setPassword(redisConfig.getPassword());
        }
        return Redisson.create(config);

    }

    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     */
    @Bean
    DistributedLocker distributedLocker(RedissonClient redissonClient) {
        DistributedLocker locker = new RedissonDistributedLocker();
        locker.setRedissonClient(redissonClient);
        RedissLockUtil.setLocker(locker);
        return locker;
    }


}
