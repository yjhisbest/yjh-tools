package com.yjh.tools.demo.controller;

import com.yjh.tools.demo.dto.UserDto;
import com.yjh.tools.demo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/custom/cache")
public class CustomCacheController {

    @RequestMapping("/getUserDtos")
    public Result<List<UserDto>> getUserDtos() {
        List<UserDto> userDtos = ((CustomCacheController) AopContext.currentProxy()).buiidUserDtos();
        return Result.success(userDtos);
    }

    /**
     * 用户和以前的Cacheable注解意义
     * 只是value读取redis-cache.properties的app.cache.redis.items中对应的name来设置缓存的时间
     * @return
     */
    @Cacheable(value = "config:", key = "'buiidUserDtos'")
    public List<UserDto> buiidUserDtos() {
        List<UserDto> userDtos = new ArrayList<>(3);
        userDtos.add(new UserDto(1, "张三"));
        userDtos.add(new UserDto(2, "李四"));
        userDtos.add(new UserDto(3, "王五"));
        return userDtos;
    }
}
