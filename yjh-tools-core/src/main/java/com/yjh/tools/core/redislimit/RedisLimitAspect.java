package com.yjh.tools.core.redislimit;

import com.yjh.tools.core.exception.RedisLimitException;
import com.yjh.tools.core.util.AspectUtil;
import com.yjh.tools.core.util.RequestUtil;
import com.yjh.tools.core.util.SpelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
public class RedisLimitAspect {

    private static final String LIMIT_PREFIX= "limit:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Around("@annotation(com.yjh.tools.core.redislimit.RedisLimit)")
    public Object limitMethod(ProceedingJoinPoint point) throws Throwable {
        // 获取方法
        Method method = AspectUtil.getMethod(point);
        // 从注解中获取spel字符串
        RedisLimit redisLimitAnnotation = method.getAnnotation(RedisLimit.class);
        RedisLimitType redisLimitType = redisLimitAnnotation.redisLimitType();
        String limitKey;
        if (RedisLimitType.IP.equals(redisLimitType)) {
            limitKey = String.format("%s%s:%s", LIMIT_PREFIX, method.getName(), RequestUtil.getIp());
        } else if (RedisLimitType.HEADER.equals(redisLimitType)) {
            String[] headers = redisLimitAnnotation.headers();
            if (headers.length <= 0) {
                log.error("method:{} 没有设置 RedisLimit对应请求头类型的限流headers", method.getName());
                throw new RedisLimitException("没有设置好RedisLimit的headers");
            }
            StringBuilder limitKeySb = new StringBuilder(method.getName());
            limitKeySb.append(":");
            for (String header : headers) {
                limitKeySb.append("_").append(header).append("_").append(RequestUtil.getHeader(header));
            }
            limitKey = String.format("%s%s", LIMIT_PREFIX, limitKeySb.toString());
        } else {
            // 获取方法参数值
            Object[] arguments = point.getArgs();
            // 创建虚拟容器
            EvaluationContext evaluationContext = SpelUtil.buildEvaluationContext(method, arguments);
            limitKey = SpelUtil.parseSpel(redisLimitAnnotation.key(), evaluationContext, String.class, "");
            if (StringUtils.isBlank(limitKey)) {
                log.error("method:{} 没有设置 RedisLimit对应自定义类型的限流key", method.getName());
                throw new RedisLimitException("没有设置好RedisLimit的key");
            }
            limitKey = String.format("%s%s", LIMIT_PREFIX, limitKey);
        }
        Long increment = stringRedisTemplate.opsForValue().increment(limitKey, 1);
        if (increment.equals(1L)) {
            // 设置生存时间
            stringRedisTemplate.expire(limitKey, redisLimitAnnotation.timeScope(), TimeUnit.SECONDS);
        } else if (increment <= 3L) {
            Long expire = stringRedisTemplate.getExpire(limitKey);
            if (expire.equals(-1L)) {
                // 以防万一没有成功设置成功ttl，重新设置
                stringRedisTemplate.expire(limitKey, redisLimitAnnotation.timeScope(), TimeUnit.SECONDS);
            }
        }
        long limitTimes = redisLimitAnnotation.limitTimes();
        if (increment > limitTimes) {
            log.error("limitKey:{}的操作次数:{} 超过限制次数{}, 开启限流", limitKey, increment, limitTimes);
            int lockTime = redisLimitAnnotation.lockTime();
            if (increment == limitTimes + 1 && lockTime > 0) {
                // 重置limitKey的有效时间为lockTime， 限流有效时间
                stringRedisTemplate.expire(limitKey, lockTime, TimeUnit.SECONDS);
            }
            throw new RedisLimitException(redisLimitAnnotation.errorMsg());
        }
        return point.proceed();
    }
}
