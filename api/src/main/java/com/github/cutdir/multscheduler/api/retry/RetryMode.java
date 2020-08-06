package com.github.cutdir.multscheduler.api.retry;

import com.github.cutdir.multscheduler.api.AbstractSchedulerMode;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Description: 在指定一段时间内不停重试调用方法，直到方法成功或者到达最大时间，线程被打中断标记，跳出循环结束方法
 *
 * @author cutdir
 */
public class RetryMode extends AbstractSchedulerMode {

	private RetryProperty retryProperty;
	private Callable command;
	private final ScheduledExecutorService scheduler;

	public RetryMode(String methodName, RetryProperty retryProperty, Callable command) {
		this.retryProperty = retryProperty;
		this.command = command;
		scheduler = new ScheduledThreadPoolExecutor(2,
				new ThreadFactoryBuilder()
						.setNameFormat(methodName + "-RetryMode-ScheduledThreadPoolExecutor-%d")
						.setDaemon(true)
						.build());
	}

	@Override
	public void submitTask() {
		long deadline = System.currentTimeMillis() + retryProperty.getMaxRetryMillis();
		try {
			//交给主线程执行
			command.call();
		} catch (Exception e) {
			if (System.currentTimeMillis() < deadline) {
				InterruptTask task = new InterruptTask(scheduler, deadline - System.currentTimeMillis());
				while (!Thread.interrupted()) {
					try {
						command.call();
						break;
					} catch (Exception ee) {
						if (System.currentTimeMillis() < deadline) {
							Thread.yield();
						} else {
							break;
						}
					}
				}
				task.cancel();
			}
		}
	}
}
