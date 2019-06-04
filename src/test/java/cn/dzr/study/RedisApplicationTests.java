package cn.dzr.study;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {

//		redisTemplate.opsForValue().set("redis", "redis");
//		redisTemplate.expire("redis", 1, TimeUnit.MINUTES);
//		System.err.println(redisTemplate.opsForValue().get("redis"));

        redisTemplate.opsForList().rightPush("list", 1);
        System.err.println(redisTemplate.hasKey("list"));
        redisTemplate.expire("list", 60, TimeUnit.SECONDS);

        List<Integer> list = redisTemplate.opsForList().range("list", 0, -1);
        list.forEach(System.err::println);
    }

}
