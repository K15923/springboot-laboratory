package com.k.lab02redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

/**
 * @author k 2023/5/9 23:40
 */
@Configuration
public class RedisConfig {


    /**
     * 手动配置redis连接
     *
     * @return
     */
    // @Bean
    // public RedissonClient redissonClient(){
    //     Config config = new Config();
    //     config.setTransportMode(TransportMode.NIO);
    //     SingleServerConfig singleServerConfig = config.useSingleServer();
    //     //可以用"rediss://"来启用SSL连接
    //     singleServerConfig.setAddress("redis://127.0.0.1:6379");
    //     singleServerConfig.setPassword("123456");
    //     RedissonClient redisson = Redisson.create(config);
    //     return redisson;
    // }

    // /**
    //  * 配置文件 application.yaml 配置
    //  *
    //  * @return
    //  */
    // @Bean
    // public RedissonClient redissonClient() {
    //     return Redisson.create();
    // }


    /**
     * 配置文件 redisson.yml 配置
     *
     * @return
     */
    @Bean
    public RedissonClient redissonClient() {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("redisson.yml");
        Config config = null;
        try {
            config = Config.fromYAML(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Redisson.create(config);
    }


}
