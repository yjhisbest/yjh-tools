package com.yjh.tools.demo.controller;

import com.yjh.tools.core.redislock.RedisLock;
import com.yjh.tools.demo.dto.UserDto;
import com.yjh.tools.demo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 分布式锁demo
 */
@Slf4j
@RestController
@RequestMapping("/redis/lock")
public class RedisLockController {

    private static final long SLEEP_TIME = 5000L;

    @RedisLock(key = "'testLock'")
    @RequestMapping("/testLock")
    public Result testLock() throws InterruptedException {
        log.info("enter testLock");
        Thread.sleep(SLEEP_TIME);
        return Result.success();
    }

    /**
     * 当@EnableRedisLock(openWatchDog = true)，openWatchDog为true,没有另外设置expires时间，即开启watch dog.
     * watch dog工作：默认锁30秒，watch dog每10秒检查一次，如果锁未释放，即为重新锁定30秒
     *
     * @param userDto
     * @return
     * @throws InterruptedException
     */
    @RedisLock(key = "'testLockUserDto_' + #userDto.uid")
    @RequestMapping("/testLockUserDto")
    public Result testLockUserDto(@RequestBody @Valid UserDto userDto) throws InterruptedException {
        log.info("enter testLockUserDto");
        log.info("userDto：{}", userDto.toString());
        Thread.sleep(SLEEP_TIME);
        return Result.success();
    }

    /**
     * 当uid大于0，分布式锁注解才生效，自定义锁持有时间为10秒，等待时间为3秒
     * @param userDto
     * @return
     * @throws InterruptedException
     */
    @RedisLock(key = "'testLockUserDto2_' + #userDto.uid", expires = 10000, waitTime = 3000,
            condition = "#userDto.uid > 0",
            errorMsg = "'请不要对uid：' + #userDto.uid + '进行重复操作'")
    @RequestMapping("/testLockUserDto2")
    public Result testLockUserDto2(@RequestBody @Valid UserDto userDto) throws InterruptedException {
        log.info("enter testLockUserDto");
        log.info("userDto：{}", userDto.toString());
        Thread.sleep(SLEEP_TIME);
        return Result.success();
    }

}
