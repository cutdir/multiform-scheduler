package com.github.cutdir.multscheduler.server.controller;

import com.github.cutdir.multscheduler.api.retry.RetryMode;
import com.github.cutdir.multscheduler.api.retry.RetryProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

/**
 * Description: RetryController
 *
 * @author cutdir
 */
@Slf4j
@RestController
public class RetryController {

	@Autowired
	private RetryProperty retryProperty;

	@GetMapping("/retry")
	public void printRetry() {
		RetryMode retryMode = new RetryMode("myRetry", retryProperty, new RetryCommandImpl());
		retryMode.submitTask();
	}

	class RetryCommandImpl implements Callable {

		@Override
		public Object call() throws Exception {
			log.info("这里是测试RetryMode运行,方法执行时间为" + LocalDateTime.now());
			Thread.sleep(1000);
			throw new Exception("测试抛出异常");
		}
	}
}
