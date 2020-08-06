package com.github.cutdir.multscheduler.server.controller;

import com.github.cutdir.multscheduler.api.selfadaption.SelfAdaptionMode;
import com.github.cutdir.multscheduler.api.selfadaption.SelfAdaptionProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 自适应调度调用
 *
 * @author cutdir
 */
@Slf4j
@RestController
public class SelfAdaptionController {

	@Autowired
	private SelfAdaptionProperty selfAdaptionProperty;

	@GetMapping("/self-adaption")
	public void printSelfAdaption() {
		SelfAdaptionMode selfAdaptionMode = new SelfAdaptionMode("mySelfAdaption", selfAdaptionProperty, new SelfAdaptionCommand());
		selfAdaptionMode.submitTask();
	}

	class SelfAdaptionCommand implements Runnable {
		@Override
		public void run() {
			log.info("这里是测试SelfAdaptionMode运行,方法执行时间为" + System.currentTimeMillis());
		}
	}
}
