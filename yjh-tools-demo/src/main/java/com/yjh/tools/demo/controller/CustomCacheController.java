package com.yjh.tools.demo.controller;

import com.yjh.tools.demo.dto.UserDto;
import com.yjh.tools.demo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/custom/cache")
public class CustomCacheController {

    public static List<Integer> uids = Arrays.asList(1,2);

    @RequestMapping("/getUserDtos")
    public Result<List<UserDto>> getUserDtos(int uid) {
        List<UserDto> userDtos = ((CustomCacheController) AopContext.currentProxy()).buiidUserDtos(uid);
        return Result.success(userDtos);
    }

    /**
     * 用户和以前的Cacheable注解意义
     * 只是value读取redis-cache.properties的app.cache.redis.items中对应的name来设置缓存的时间
     *
     * condition 当uid不在集合uids中才走缓存
     * @return
     */
    @Cacheable(value = "config:", key = "'buiidUserDtos'", condition = "!T(com.yjh.tools.demo.controller.CustomCacheController).uids.contains(#uid)")
    public List<UserDto> buiidUserDtos(int uid) {
        List<UserDto> userDtos = new ArrayList<>(3);
        userDtos.add(new UserDto(1, "张三"));
        userDtos.add(new UserDto(2, "李四"));
        userDtos.add(new UserDto(3, "王五"));
        return userDtos;
    }
}
