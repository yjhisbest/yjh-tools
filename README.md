# yjh-tool-core
该模块包含：
1，分布式锁：基于redis和自定义的RedisLock注解实现流控
2，限流：基于redis和自定义的RedisLimit注解实现流控
3，自定义缓存：redis
4，跨域配置：导入@EnableCorsFilter即可在项目中完成跨域处理
5，雪花算法工具：改工具遇到导入@EnableSnowflake注解开启，一秒钟能生产1024个雪花值

# yjh-tool-demo
该模块针对yjh-tool-core的功能做出了对应的demo

# 说明
分布式锁利用redisson实现
RedissonClient初始化由RedissonAutoConfiguration实现。
由于本项目还利用redis实现了缓存和限流等功能，
所以配置的时候推荐按照像springboot正常配置redis即可。
redission会读取pringboot正常配置redis的信息用于初始化RedissonClient。