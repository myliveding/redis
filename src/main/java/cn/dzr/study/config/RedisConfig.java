package cn.dzr.study.config;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @Auther: dingzr
 * @Date: 2019/6/4 14:33
 * @Description: 解决存储时key和value的乱码
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {

    private String host;

    private Integer port;

    private String password;

    private Integer timeout;

    private Integer database;

    private Integer databaseListen;


    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
    @Scope(value = "prototype")
    public GenericObjectPoolConfig lettucePool() {
        return new GenericObjectPoolConfig();
    }

    /**
     * 组装redis工厂
     */
    private LettuceConnectionFactory getLettuceConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig, Integer database) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeout))
                .poolConfig(genericObjectPoolConfig)
                .build();
        return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory(lettucePool()));
        setSerializer(redisTemplate);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig) {
        return getLettuceConnectionFactory(genericObjectPoolConfig, database);
    }

    @Bean(name = "redisTemplateForListen")
    public RedisTemplate<String, Object> redisTemplateForListen() {
        RedisTemplate<String, Object> redisTemplateForListen = new RedisTemplate<>();
        redisTemplateForListen.setConnectionFactory(redisConnectionFactoryListen(lettucePool()));
        setSerializer(redisTemplateForListen);
        redisTemplateForListen.afterPropertiesSet();
        return redisTemplateForListen;
    }


    @Bean
    public RedisConnectionFactory redisConnectionFactoryListen(GenericObjectPoolConfig genericObjectPoolConfig) {
        return getLettuceConnectionFactory(genericObjectPoolConfig, databaseListen);
    }

    private void setSerializer(RedisTemplate<String, Object> template) {
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
    }

}
