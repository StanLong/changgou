package com.changou.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author 矢量
 * @date 2020/2/8-15:26
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //排除掉数据库自动加载
@EnableEurekaClient
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }
}
