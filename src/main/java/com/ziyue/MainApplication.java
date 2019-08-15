package com.ziyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableAsync//开启异步功能 （添加日志到数据库中 @Async ）
@EnableScheduling//开启定时任务（ScheduledService类中）
@EnableTransactionManagement//开启事务管理功能
@EnableCaching //开启基于注解的缓存
@SpringBootApplication
public class MainApplication /** extends SpringBootServletInitializer */ {
	public static void main(String[] args) {
	    // Spring应用启动起来
		SpringApplication.run(MainApplication.class,args);
	}
	
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//	    return application.sources(MainApplication.class);
//	}

}
