package com.yjh.tools.core.redislimit;

import java.util.Objects;

public enum RedisLimitExcuteType {

    /**
     * 自增
     */
    INCREMENT(1, "自增"),
    /**
     * 滑动窗口
     */
    SLIDING_WINDOW(2, "滑动窗口"),
    ;

    private final Integer type;
    private final String name;

    RedisLimitExcuteType(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static RedisLimitExcuteType getByType(Integer type) {
        for (RedisLimitExcuteType redisLimitExcuteType : values()) {
            if (Objects.equals(redisLimitExcuteType.getType(), type)) {
                return redisLimitExcuteType;
            }
        }
        return null;
    }
}
