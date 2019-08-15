package com.ziyue.test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoggingApplicationTests {
	
	//日志记录器
	//Logger log = LoggerFactory.getLogger(getClass());
	
	
	@Test
	public void contextLoads() {
		//日志的级别；
		//由低到高   trace<debug<info<warn<error
		//可以调整输出的日志级别；日志就只会在这个级别以以后的高级别生效
		log.trace("这是trace日志...");
		log.debug("这是debug日志...");
		//SpringBoot默认给我们使用的是info级别的，没有指定级别的就用SpringBoot默认规定的级别；root级别
		log.info("这是info日志...");
		log.warn("这是warn日志...");
		log.error("这是error日志...");
	}
}
