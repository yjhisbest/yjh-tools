package com.yjh.tools.core.redislock;

import com.yjh.tools.core.exception.RedisLockException;
import com.yjh.tools.core.util.AspectUtil;
import com.yjh.tools.core.util.SpelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.EvaluationContext;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Aspect
public class RedisLockAspect {

    /**
     * 开门狗开关
     */
    protected static boolean openWatchDog = false;

    /**
     * 锁默认存活时间
     */
    protected final static int LOCK_DEFAULT_EXPIRES = 30000;

    @Resource
    private RedissonClient redissonClient;

    @Around("@annotation(com.yjh.tools.core.redislock.RedisLock)")
    public Object lockMethod(ProceedingJoinPoint point) throws Throwable {
        // 获取方法参数值
        Object[] arguments = point.getArgs();
        // 获取方法
        Method method = AspectUtil.getMethod(point);
        // 从注解中获取spel字符串
        RedisLock redisLockAnnotation = method.getAnnotation(RedisLock.class);
        String redisKey = redisLockAnnotation.key();
        // 锁持有时间
        int expires = redisLockAnnotation.expires();
        // 等待锁时间
        int waitTime = redisLockAnnotation.waitTime();
        // 创建虚拟容器
        EvaluationContext evaluationContext = SpelUtil.buildEvaluationContext(method, arguments);
        // 解析spel表达式，判断condition条件是否生效
        Boolean isLock = SpelUtil.parseSpel(redisLockAnnotation.condition(), evaluationContext, Boolean.class, true);
        if (!isLock) {
            return point.proceed();
        }
        // 解析spel表达式，生成锁的key
        redisKey = SpelUtil.parseSpel(redisKey, evaluationContext, String.class, "");
        if (StringUtils.isBlank(redisKey)) {
            log.error("method:{} 没有设置 RedisLock的key", method.getName());
            throw new RedisLockException("没有设置好RedisLock的key");
        }
        RLock lock = redissonClient.getLock(redisKey);
        boolean isOnLock = false;
        try {
            //尝试加锁
            if (openWatchDog && expires == LOCK_DEFAULT_EXPIRES) {
                // 尝试拿锁10s后停止重试,返回false
                // 具有Watch Dog 自动延期机制 默认续30s
                // 只要锁成功，就会启动定时任务（重新给锁设置过期时间）新的时间就是看门狗（watch dog）的默认时间，每10秒，都会自动续期续成满时间
                isOnLock = lock.tryLock(waitTime, TimeUnit.MILLISECONDS);
            } else {
                isOnLock = lock.tryLock(waitTime, expires, TimeUnit.MILLISECONDS);
            }
            if (!isOnLock) {
                // 输出锁住的错误信息
                String errorMsg = String.format("当前资源已被锁定，无法继续进行当前操作。redisKey:%s,method:%s,args:%s",
                        redisKey, method.getName(), Stream.of(arguments)
                                .map(Object -> {if (Object != null) {return Object.toString();} else {return "null";}})
                                .collect(Collectors.joining(",")));
                log.error(errorMsg);
                String msgSpel = redisLockAnnotation.errorMsg();
                if (StringUtils.isNotBlank(msgSpel)) {
                    // 自定义了错误信息，解析spel表达式, 获取锁定资的时候的报错信息，返回客户端
                    String msg = SpelUtil.parseSpel(msgSpel, evaluationContext, String.class, msgSpel);
                    throw new RedisLockException(msg);
                }
                throw new RedisLockException("Don't repeat the operation");
            }
            log.info("locked redisKey {}", redisKey);
            return point.proceed();
        } finally {
            try {
                if (isOnLock) {
                    lock.unlock();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
