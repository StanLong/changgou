package com.changgou.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author 矢量
 * @date 2020/2/6-17:11
 */
@SpringBootApplication
@EnableEurekaServer // 开启eureka服务
public class EurekaApplication {
    /**
     * 加载启动类
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
