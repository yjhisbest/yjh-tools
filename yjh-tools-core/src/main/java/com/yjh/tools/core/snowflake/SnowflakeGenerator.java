package com.yjh.tools.core.snowflake;

import com.yjh.tools.core.exception.SnowflakeException;
import com.yjh.tools.core.util.CodeGeneratorUtil;
import org.joda.time.DateTime;

/**
 * 雪花算法构造器
 *
 * @author yjh
 */
public class SnowflakeGenerator {

    /**
     * 起始年
     */
    private static final int START_YEAR = 2020;

    /**
     * 月占位长度
     */
    private static final int BIT_LEN_MONTH = 4;

    /**
     * 日占位长度
     */
    private static final int BIT_LEN_DAY = 5;

    /**
     * 时占位长度
     */
    private static final int BIT_LEN_HOUR = 5;

    /**
     * 分占位长度
     */
    private static final int BIT_LEN_MINUTE = 6;

    /**
     * 秒占位长度
     */
    private static final int BIT_LEN_SECOND = 6;

    /**
     * 机器ID占位长度
     */
    private static final int BIT_LEN_MACHINE_ID = 4;

    /**
     * 自增ID占位长度
     */
    private static final int BIT_LEN_IDENTITY = 10;

    /**
     * 业务占位长度
     */
    private static final int BIT_LEN_BUSINESS = 5;

    /**
     * 最大自增ID
     */
    private static final long MAX_IDENTITY = (1 << BIT_LEN_IDENTITY) - 1;

    /**
     * 上次生成时间
     */
    private static long past = -1L;

    /**
     * 自增ID
     */
    private static long identity = 0L;

    /**
     * 默认业务类型：0
     */
    private static int DEFAULT_BUSINESS_TYPE = 0;

    /**
     * 生成递增ID
     *
     * @return
     */
    private static synchronized long generate() {
        DateTime dateTime = DateTime.now();
        int year = dateTime.getYear() - START_YEAR;
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay();
        int minute = dateTime.getMinuteOfHour();
        int second = dateTime.getSecondOfMinute();
        long now = ((((year << BIT_LEN_MONTH | month) << BIT_LEN_DAY | day) << BIT_LEN_HOUR | hour) << BIT_LEN_MINUTE
                | minute) << BIT_LEN_SECOND | second;
        if (now != past) {
            identity = 0L;
            past = now;
        } else {
            identity++;
            if (identity > MAX_IDENTITY) {
                throw new SnowflakeException("identity out of bounds");
            }
        }
        Integer machineId = MachineIdManager.getMachineId();
        return (now << BIT_LEN_MACHINE_ID | machineId) << BIT_LEN_IDENTITY | identity;
    }

    /**
     * 生成唯一业务id
     *
     * @param businessType 业务类型：取值范围[0,31]
     *
     * @return
     */
    public static Long generateId(int businessType) {
        return generate() << BIT_LEN_BUSINESS | businessType;
    }

    /**
     * 生成唯一业务码
     *
     * @param businessType 业务类型：取值范围[0,31]
     * @return
     */
    public static String generateCode(int businessType) {
        return CodeGeneratorUtil.toSerialCode(generateId(businessType));
    }

    /**
     * 生成默认类型业务码
     *
     * @return
     */
    public static String generateCode() {
        return generateCode(DEFAULT_BUSINESS_TYPE);
    }

    public static void main(String[] args) {
        System.out.println(generateCode());
    }

}
