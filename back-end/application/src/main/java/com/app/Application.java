package com.app;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Indexed;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: anyone
 * @since: 2023/9/16
 * @description:  启动类
 */
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@Slf4j
@Indexed
@MapperScan("**.mapper")
@SpringBootApplication
@EnableCaching
public class Application {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
            log.info("项目启动成功(ง ˙o˙)ว");
        } catch (Exception e) {
            log.error("启动失败:",e);
        }
    }
}
