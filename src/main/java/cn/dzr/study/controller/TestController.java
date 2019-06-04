package cn.dzr.study.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: dingzr
 * @Date: 2019/6/4 17:12
 * @Description:
 */
@RestController
@RequestMapping("/api/redis")
@Api(value = "TestController", tags = "TestController", description = "Redis 测试")
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping(value = "test", produces = "application/json; charset=utf-8")
    @ApiOperation(value = "测试", httpMethod = "POST", produces = "application/json; charset=utf-8")
    public Map<String, Object> getCreditScoreHomePage(@ApiParam("key") @RequestParam String key,
                                                      @ApiParam("value") @RequestParam String value) {

        redisTemplate.opsForList().rightPush(key, value);
        System.err.println(redisTemplate.hasKey(key));
        redisTemplate.expire(key, 60, TimeUnit.SECONDS);

        return null;
    }


}
