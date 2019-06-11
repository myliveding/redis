package cn.dzr.study;

import cn.dzr.study.service.DistributedLocker;
import cn.dzr.study.util.RedissonLockUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DistributedLocker distributedLocker;


    @Test
    public void contextLoadsRedis() {
        redisTemplate.opsForValue().set("redis", "redis");
        redisTemplate.expire("redis", 1, TimeUnit.MINUTES);
        System.err.println(redisTemplate.opsForValue().get("redis"));
    }


    @Test
    public void contextLoadsServiceImpl() {
        String lockKey = "testRedisson";//分布式锁的key
        //执行的业务代码
        try {
//            for (int i = 0; i < 10; i++) {
                //设置60秒自动释放锁  （默认是30秒自动过期）
            distributedLocker.lock(lockKey, TimeUnit.SECONDS, 29);
                System.err.println("执行业务逻辑...");
            Thread.sleep(1000);
                distributedLocker.unlock(lockKey); //释放锁
//            }
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
            RedissonLockUtil.lock(lockKey, TimeUnit.SECONDS, 29);
            System.err.println("执行业务逻辑...");

            Thread.sleep(1000);

            RedissonLockUtil.unlock(lockKey); //释放锁
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
