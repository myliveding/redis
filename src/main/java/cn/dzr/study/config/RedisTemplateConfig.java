package cn.dzr.study.config;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.Duration;

/**
 * @Auther: dingzr
 * @Date: 2018/12/24 10:20
 * @Description: 解决key存储到redis中，控制台查看乱码
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "spring.redis")
@Validated
public class RedisTemplateConfig {

    @NotNull
    private Integer database;

    @NotNull
    private Integer databaseListen;

    @NotNull
    private String host;

    @NotNull
    private Integer port;

    private String passWord;

    @NotNull
    private Integer timeout;

    @NotNull
    private Integer maxActive;

    @NotNull
    private Integer maxWait;

    @NotNull
    private Integer maxIdle;

    @NotNull
    private Integer minIdle;

    /**
     * GenericObjectPoolConfig 连接池配置
     */
    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);
        return genericObjectPoolConfig;
    }

    /**
     * 组装redis工厂
     */
    private LettuceConnectionFactory getLettuceConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig, Integer database) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(passWord));

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeout))
                .poolConfig(genericObjectPoolConfig)
                .build();
        return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
    }

    @Bean
    @Primary
    @Qualifier
    public RedisConnectionFactory redisConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig) {
        return getLettuceConnectionFactory(genericObjectPoolConfig, database);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactoryListen(GenericObjectPoolConfig genericObjectPoolConfig) {
        return getLettuceConnectionFactory(genericObjectPoolConfig, databaseListen);
    }

    @Bean(name = "redisTemplate")
    @Primary
    @Qualifier
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplateForListen = new RedisTemplate<>();
        redisTemplateForListen.setConnectionFactory(redisConnectionFactory(genericObjectPoolConfig()));
        setSerializer(redisTemplateForListen);
        redisTemplateForListen.afterPropertiesSet();
        return redisTemplateForListen;
    }


    @Bean(name = "redisTemplateForListen")
    public RedisTemplate<String, Object> redisTemplateForListen() {
        RedisTemplate<String, Object> redisTemplateForListen = new RedisTemplate<>();
        redisTemplateForListen.setConnectionFactory(redisConnectionFactoryListen(genericObjectPoolConfig()));
        setSerializer(redisTemplateForListen);
        redisTemplateForListen.afterPropertiesSet();
        return redisTemplateForListen;
    }

    private void setSerializer(RedisTemplate<String, Object> template) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
    }


}
