package cn.dzr.study;

import cn.dzr.study.service.DistributedLocker;
import cn.dzr.study.util.RedissLockUtil;
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

    @Autowired
    private RedisTemplate redisTemplateForListen;

    @Autowired
    private DistributedLocker distributedLocker;


    @Test
    public void contextLoadsRedis() {

        redisTemplate.opsForValue().set("redis", "redis");
        redisTemplate.expire("redis", 1, TimeUnit.MINUTES);
        System.err.println(redisTemplate.opsForValue().get("redis"));

        redisTemplateForListen.opsForValue().set("redis", "redis");
        redisTemplateForListen.expire("redis", 1, TimeUnit.MINUTES);
        System.err.println(redisTemplateForListen.opsForValue().get("redis"));

    }


    @Test
    public void contextLoadsServiceImpl() {
        String lockKey = "testRedisson";//分布式锁的key
        //执行的业务代码
        try {
            for (int i = 0; i < 10; i++) {
                //设置60秒自动释放锁  （默认是30秒自动过期）
                distributedLocker.lock(lockKey, TimeUnit.SECONDS, 60);
                System.err.println("执行业务逻辑...");
                Thread.sleep(10000);
                distributedLocker.unlock(lockKey); //释放锁
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void contextLoadsUtil() {
        String lockKey = "testRedisson-1";//分布式锁的key
        try {
            //执行的业务代码
//        for (int i = 0; i < 10; i++) {
            //设置60秒自动释放锁  （默认是30秒自动过期）
            RedissLockUtil.lock(lockKey, TimeUnit.SECONDS, 60);
            System.err.println("执行业务逻辑...");

            Thread.sleep(3000);

            RedissLockUtil.unlock(lockKey); //释放锁
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
