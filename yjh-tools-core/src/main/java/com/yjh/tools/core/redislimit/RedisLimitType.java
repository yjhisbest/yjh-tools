package com.yjh.tools.core.redislimit;

import java.util.Objects;

/**
 * 限制类型
 *
 * @author yjh
 */
public enum RedisLimitType {

    /**
     * 客户端自定义
     */
    CUSTOMER(1, "自定义"),
    /**
     * ip
     */
    IP(2, "ip"),
    /**
     * 请求头参数
     */
    HEADER(3, "请求头"),
    ;

    private final Integer code;
    private final String name;

    RedisLimitType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static RedisLimitType getByCode(Integer code) {
        for (RedisLimitType limitType : values()) {
            if (Objects.equals(limitType.getCode(), code)) {
                return limitType;
            }
        }

        return null;
    }

}
