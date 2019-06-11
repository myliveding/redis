package cn.dzr.study.config;

import cn.dzr.study.service.DistributedLocker;
import cn.dzr.study.service.impl.RedissonDistributedLocker;
import cn.dzr.study.util.RedissonLockUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @Auther: dingzr
 * @Date: 2019/6/5 10:59
 * @Description:
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private Environment env;

    /**
     * 单机模式自动装配
     */
    @Bean
    RedissonClient redissonCluster() throws IOException {
        String[] profiles = env.getActiveProfiles();
        String profile = "-dev";
        if (profiles.length > 0) {
            profile = "-" + profiles[0];
        }
        Config config = Config.fromYAML(new ClassPathResource("redisson" + profile + ".yml").getInputStream());
        return Redisson.create(config);
    }


    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     */
    @Bean
    DistributedLocker distributedLocker(RedissonClient redissonClient) {
        DistributedLocker locker = new RedissonDistributedLocker();
        locker.setRedissonClient(redissonClient);
        RedissonLockUtil.setLocker(locker);
        return locker;
    }


}
