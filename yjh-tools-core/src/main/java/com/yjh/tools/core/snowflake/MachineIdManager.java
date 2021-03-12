package com.yjh.tools.core.snowflake;

import com.yjh.tools.core.exception.SnowflakeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;

/**
 * 给每个实例分配一个机器码id，id取值范围[0, 15]
 * 也可以直接给实例配置yjh.tools.core.custom.machineId设置不同实例的机器码id，取值范围[0, 15]
 */
@Slf4j
public class MachineIdManager {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * redis对应机器码key
     */
    protected static String machineIdKey;

    /**
     * 机器ID占位长度
     */
    private static final int BIT_LEN_MACHINE_ID = 4;

    /**
     * 自定义机器ID
     */
    @Value("${yjh.tools.core.custom.machineId:-1}")
    private Integer customMachineId;

    /**
     * 机器ID
     */
    private static Integer machineId = -1;

    @PostConstruct
    public void init() {
        if (StringUtils.isBlank(machineIdKey)) {
            throw new SnowflakeException("请配置redis对应机器码key：snowflake.machineIdKey");
        }
        if (customMachineId.equals(-1)) {
            Integer redisId = stringRedisTemplate.opsForValue().increment(machineIdKey).intValue();
            machineId = redisId % (1 << BIT_LEN_MACHINE_ID);
            log.info("获得机器码,machineId:{}", machineId);
        } else if (customMachineId >= 0 && customMachineId <= 15){
            machineId = customMachineId;
            log.info("获得机器码,machineId:{}", machineId);
        } else {
            throw new SnowflakeException("自定义customMachineId机器码的取值范围在[0,15]");
        }
    }

    public static Integer getMachineId() {
        if (machineId.equals(-1)) {
            throw new SnowflakeException("need init machineId");
        }
        return machineId;
    }

}
