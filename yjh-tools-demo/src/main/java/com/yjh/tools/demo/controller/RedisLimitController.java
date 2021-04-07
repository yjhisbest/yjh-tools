package com.yjh.tools.demo.controller;

import com.yjh.tools.core.redislimit.RedisLimit;
import com.yjh.tools.core.redislimit.RedisLimitExcuteType;
import com.yjh.tools.core.redislimit.RedisLimitType;
import com.yjh.tools.demo.dto.UserDto;
import com.yjh.tools.demo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 限流demo
 */
@Slf4j
@RestController
@RequestMapping("/redis/limit")
public class RedisLimitController {

    /**
     * 自定义限流：RedisLimitType.CUSTOMER为默认类型，与key配合使用，key支持spel表达式
     * 默认3秒内能访问6次
     * @return
     */
    @RedisLimit(redisLimitType = RedisLimitType.CUSTOMER, key = "'testCustomLimit'")
    @RequestMapping("/testCustomLimit")
    public Result testCustomLimit() {
        log.info("enter testCustomLimit");
        return Result.success();
    }

    /**
     * 自定义限流：根据参数限流
     * 将对请求参数userDto的uid进行限流，5秒内能访问10次，
     * @return
     */
    @RedisLimit(key = "'testCustomLimitUserDto_' + #userDto.uid", timeScope = 5, limitTimes = 10, errorMsg = "操作频繁")
    @RequestMapping("/testCustomLimitUserDto")
    public Result testCustomLimitUserDto(@RequestBody @Valid UserDto userDto) {
        log.info("enter testCustomLimitUserDto");
        return Result.success();
    }

    /**
     * 自定义限流：根据参数限流并锁定
     * 将对请求参数userDto的uid进行限流，5秒内能访问10次，超过10次将锁定60秒不能再操作
     * @return
     */
    @RedisLimit(key = "'testCustomLimitAndLockUserDto_' + #userDto.uid", timeScope = 5, limitTimes = 10, lockTime = 60, errorMsg = "操作频繁")
    @RequestMapping("/testCustomLimitAndLockUserDto")
    public Result testCustomLimitAndLockUserDto(@RequestBody @Valid UserDto userDto) {
        log.info("enter testCustomLimitAndLockUserDto");
        return Result.success();
    }

    /**
     * 请求头限流：RedisLimitType.HEADER需要与headers配合使用
     * 默认3秒内能访问6次
     * @return
     */
    @RedisLimit(redisLimitType = RedisLimitType.HEADER, headers = {"accessToken"})
    @RequestMapping("/testHeadLimit")
    public Result testHeadLimit() {
        log.info("enter testHeadLimit");
        return Result.success();
    }

    /**
     * 请求头限流：设置限流注解参数
     * 5秒钟能访问10次
     * @return
     */
    @RedisLimit(redisLimitType = RedisLimitType.HEADER, headers = {"accessToken"}, timeScope = 5, limitTimes = 10, errorMsg = "操作频繁")
    @RequestMapping("/testHeadAnnotationParamLimit")
    public Result testHeadAnnotationParamLimit() {
        log.info("enter testHeadAnnotationParamLimit");
        return Result.success();
    }

    /**
     * 请求头限流：设置限流注解参数和锁定操作
     * 5秒钟能访问10次,超过10次将锁定60秒不能再操作
     * @return
     */
    @RedisLimit(redisLimitType = RedisLimitType.HEADER, headers = {"accessToken"}, timeScope = 5, limitTimes = 10, lockTime = 60, errorMsg = "操作频繁")
    @RequestMapping("/testHeadAnnotationParamLimitAndLock")
    public Result testHeadAnnotationParamLimitAndLock() {
        log.info("enter testHeadAnnotationParamLimitAndLock");
        return Result.success();
    }

    /**
     * ip限流：将根据请求方ip进行限流
     * 默认3秒内能访问6次
     * @return
     */
    @RedisLimit(redisLimitType = RedisLimitType.IP)
    @RequestMapping("/testIpLimit")
    public Result testIpLimit() {
        log.info("enter testIpLimit");
        return Result.success();
    }

    /**
     * ip限流：自定义限流参数
     * 5秒钟能访问10次
     * @return
     */
    @RedisLimit(redisLimitType = RedisLimitType.IP, timeScope = 5, limitTimes = 10, errorMsg = "操作频繁")
    @RequestMapping("/testIpAnnotationParamLimit")
    public Result testIpAnnotationParamLimit() {
        log.info("enter testIpAnnotationParamLimit");
        return Result.success();
    }

    /**
     * ip限流：设置限流注解参数和锁定操作
     * 5秒钟能访问10次,超过10次将锁定60秒不能再操作
     * @return
     */
    @RedisLimit(redisLimitType = RedisLimitType.IP, timeScope = 5, limitTimes = 10, lockTime = 60, errorMsg = "操作频繁")
    @RequestMapping("/testIpAnnotationParamLimitAndLock")
    public Result testIpAnnotationParamLimitAndLock() {
        log.info("enter testIpAnnotationParamLimitAndLock");
        return Result.success();
    }

    /**
     * 滑动窗口限流：ip限流：自定义限流参数
     * 30秒钟能访问10次
     * @return
     */
    @RedisLimit(redisLimitType = RedisLimitType.IP, redisLimitExcuteType = RedisLimitExcuteType.SLIDING_WINDOW, timeScope = 30, limitTimes = 10, errorMsg = "操作频繁")
    @RequestMapping("/testIpAnnotationParamSlidingWindowLimit")
    public Result testIpAnnotationParamSlidingWindowLimit() {
        log.info("enter testIpAnnotationParamLimit");
        return Result.success();
    }

}
