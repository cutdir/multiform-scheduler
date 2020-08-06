package com.github.cutdir.multscheduler.api.gradretry;

import com.github.cutdir.multscheduler.api.AbstractSchedulerMode;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description: 在指定梯度次数内按照时间梯度间隔重试调用方法，直到方法成功或者到达最大梯度次数
 *
 * @author cutdir
 */
public class GradRetryMode extends AbstractSchedulerMode {


	private GradRetryProperty gradRetryProperty;
	private Callable command;
	private final ScheduledExecutorService scheduler;

	private static final String COMMA = ",";

	public GradRetryMode(String methodName, GradRetryProperty gradRetryProperty, Callable command) {
		this.gradRetryProperty = gradRetryProperty;
		this.command = command;
		scheduler = new ScheduledThreadPoolExecutor(2,
				new ThreadFactoryBuilder()
						.setNameFormat(methodName + "-GradRetryMode-ScheduledThreadPoolExecutor-%d")
						.setDaemon(true)
						.build());
	}

	@Override
	public void submitTask() {
		List<Long> retryTimes = Arrays.stream(gradRetryProperty.getRetryTimes().split(COMMA)).map(Long::valueOf).collect(Collectors.toList());
		try {
			command.call();
		} catch (Throwable e) {
			retryTimes.stream().forEachOrdered(time -> {
				try {
					scheduler.schedule(command, time, TimeUnit.MILLISECONDS).get();
					return;
				} catch (Throwable ee) {
					//log
				}
			});
		}

	}
}
