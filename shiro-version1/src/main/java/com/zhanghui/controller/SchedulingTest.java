package com.zhanghui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulingTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingTest.class);
	
	//定时任务 5秒执行一次
/*	@Scheduled(cron = "0/5 * * * * ?")
	public void scheduler(){
		LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>>scheduled test .....");
	}*/
}