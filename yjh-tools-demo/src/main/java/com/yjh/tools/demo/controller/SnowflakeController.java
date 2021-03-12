package com.yjh.tools.demo.controller;

import com.yjh.tools.core.snowflake.SnowflakeGenerator;
import com.yjh.tools.demo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/snowflake")
public class SnowflakeController {

    /**
     * 使用雪花算法生产id：1秒钟支持1024个
     * @return
     */
    @RequestMapping("/testSnowflakeId")
    public Result<Long> testSnowflakeId() {
        // SnowflakeGenerator.generateId的参数为业务类型。此算法1秒钟生成的id是针对所有业务类型的
        Long id = SnowflakeGenerator.generateId(0);
        return Result.success(id);
    }

    /**
     * 生成随机码：1秒钟支持1024个
     * 使用雪花算法生成id后再把id转换成唯一码
     * @return
     */
    @RequestMapping("/testRandomCode")
    public Result<String> testRandomCode() {
        // SnowflakeGenerator.generateCode的参数为业务类型。此算法1秒钟生成的code是针对所有业务类型的
        String randomCode = SnowflakeGenerator.generateCode(1);
        return Result.success(randomCode);
    }
}
