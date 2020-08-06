package com.github.cutdir.multscheduler.server.controller;

import com.github.cutdir.multscheduler.api.gradretry.GradRetryMode;
import com.github.cutdir.multscheduler.api.gradretry.GradRetryProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

/**
 * Description: GradRetryController
 *
 * @author cutdir
 */
@Slf4j
@RestController
public class GradRetryController {

	@Autowired
	private GradRetryProperty gradRetryProperty;

	@GetMapping("/grad-retry")
	public void printGradRetry() {
		GradRetryMode gradRetryMode = new GradRetryMode("myGradRetry", gradRetryProperty, new RetryCommandImpl());
		gradRetryMode.submitTask();
	}

	class RetryCommandImpl implements Callable {

		@Override
		public Object call() throws Exception {
			log.info("这里是测试GradRetryMode运行,方法执行时间为" + LocalDateTime.now());
			Thread.sleep(1000);
			throw new Exception("测试抛出异常");
		}
	}
}
