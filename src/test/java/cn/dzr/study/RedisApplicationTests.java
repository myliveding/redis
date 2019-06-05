package cn.dzr.study;

import cn.dzr.study.service.DistributedLocker;
import cn.dzr.study.util.RedissLockUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApplicationTests {

    @Autowired
    private DistributedLocker distributedLocker;


    @Test
    public void contextLoads() {
        String lockKey = "testRedisson";//分布式锁的key
        //执行的业务代码
        for (int i = 0; i < 10; i++) {
            //设置60秒自动释放锁  （默认是30秒自动过期）
            distributedLocker.lock(lockKey, TimeUnit.SECONDS, 60);
            System.err.println("执行业务逻辑...");
//            RBucket<String> bucket = redissonClient.getBucket("aaa");
//            int stock = Integer.parseInt(bucket.get());
//            if(stock > 0){
//                bucket.set(String.valueOf(stock-1));
//                System.out.println("test2_:lockkey:"+lockKey+",stock:"+(stock-1)+"");
//            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            distributedLocker.unlock(lockKey); //释放锁
        }
    }

    @Test
    public void contextLoads1() {
        String lockKey = "testRedisson-1";//分布式锁的key
        //执行的业务代码
        for (int i = 0; i < 10; i++) {
            //设置60秒自动释放锁  （默认是30秒自动过期）
            RedissLockUtil.lock(lockKey, TimeUnit.SECONDS, 60);
            System.err.println("执行业务逻辑...");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RedissLockUtil.unlock(lockKey); //释放锁
        }
    }

}
