package com.github.cutdir.multscheduler.api.selfadaption;

import com.github.cutdir.multscheduler.api.AbstractSchedulerMode;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * Description: 时间自适应模式方法调度
 *
 * @author cutdir
 */
public class SelfAdaptionMode extends AbstractSchedulerMode {

	private SelfAdaptionProperty selfAdaptionProperty;
	private Runnable task;
	private final ScheduledExecutorService scheduler;
	private final ThreadPoolExecutor executor;

	/**
	 * 读取配置文件，构建调度线程池和执行线程池
	 *
	 * @param selfAdaptionProperty 配置
	 * @param task                 待执行方法
	 */
	public SelfAdaptionMode(String methodName, SelfAdaptionProperty selfAdaptionProperty, Runnable task) {
		this.selfAdaptionProperty = selfAdaptionProperty;
		this.task = task;
		scheduler = new ScheduledThreadPoolExecutor(2,
				new ThreadFactoryBuilder()
						.setNameFormat(methodName + "-SelfAdaptionMode-ScheduledThreadPoolExecutor-%d")
						.setDaemon(true)
						.build());

		executor = new ThreadPoolExecutor(1,
				selfAdaptionProperty.getExecutorThreadPoolSize(),
				0,
				TimeUnit.SECONDS,
				new SynchronousQueue<>(),
				new ThreadFactoryBuilder()
						.setNameFormat(methodName + "SelfAdaptionMode-ThreadPoolExecutor-%d")
						.setDaemon(true)
						.build()
		);
	}

	@Override
	public void submitTask() {
		int selfAdaptionTimeout = selfAdaptionProperty.getTimeout();
		int expBackOffBound = selfAdaptionProperty.getExpBackOffBound();
		scheduler.schedule(new TimedSupervisorTask(
				scheduler,
				executor,
				selfAdaptionTimeout,
				TimeUnit.MILLISECONDS,
				expBackOffBound,
				task
		), selfAdaptionTimeout, TimeUnit.MILLISECONDS);
	}
}
