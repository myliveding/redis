package cn.dzr.study.config;


import cn.dzr.study.listener.MessageReceiver;
import cn.dzr.study.listener.RedisExpiredErrorHandler;
import cn.dzr.study.listener.RedisExpiredListener;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @Auther: dingzr
 * @Date: 2018/12/21 13:26
 * @Description: redis 监听容器配置
 */
@Configuration
@Data
public class RedisListenerContainerConfig {


    /**
     * redis 过期key常量
     * 数据库里面的键事件通知，所有通知以 keyevent@ 为前缀，针对event
     */
    private static final String KEY_EVENT_EXPIRED_TOPIC = "__keyevent@14__:expired";

    /**
     * 加载监听处理类--方法一
     *
     * @param connectionFactory 监听容器
     *                          //     * @param config            线程池配置
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
//        container.setTaskExecutor(config);
        container.addMessageListener(new RedisExpiredListener(), new PatternTopic(KEY_EVENT_EXPIRED_TOPIC));
        container.setErrorHandler(new RedisExpiredErrorHandler());
        return container;
    }

//    /**
//     * 加载监听处理类--方法二
//     * @param connectionFactory 监听容器
//     * @param listenerAdapter 处理监听的反射方法
//     * @param config 线程池配置
//     */
//    @Bean
//    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter, ThreadPoolTaskExecutor config) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setTaskExecutor(config);
//        container.addMessageListener(listenerAdapter, new PatternTopic(KEY_EVENT_EXPIRED_TOPIC));
//        container.setErrorHandler(new RedisExpiredErrorHandler());
//        return container;
//    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     *
     * @param receiver 消息处理类
     * @return listenerAdapter
     */
    @Bean
    MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        //也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage 可以自己到源码里面看
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    /**
     * redis 读取内容的template
     */
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }


}
