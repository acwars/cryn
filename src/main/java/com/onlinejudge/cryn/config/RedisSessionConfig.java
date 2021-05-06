package com.onlinejudge.cryn.config;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 注解原理：官网上有，它有一个过滤器SessionRepositoryFilter，
 * SessionRepositoryRequestWrapper会将HttpSession包装用它自己的SpringSession来代替HttpSession，
 * 如果有从redis中获取，没有就新建一个放入redis中
 */

/**
 * 该注解默认注册springSessionRepositoryFilter,该过滤器会替换容器的HttpSession实现，而其内部的实现将会采用redis。
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class RedisSessionConfig {
   /* @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }*/

}
