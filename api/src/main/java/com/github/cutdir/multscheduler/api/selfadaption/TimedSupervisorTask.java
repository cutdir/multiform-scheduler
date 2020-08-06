package com.github.cutdir.multscheduler.api.selfadaption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Description: 方法调度按照时间自适应执行,当方法执行发生超时时，下次执行时间和本次间隔会变成当前间隔时间的2倍，
 * 直到方法正常执行后，间隔时间会恢复原值
 *
 * @author cutdir
 */
public class TimedSupervisorTask extends TimerTask {
	private static final Logger logger = LoggerFactory.getLogger(TimedSupervisorTask.class);

	private final ScheduledExecutorService scheduler;
	private final ThreadPoolExecutor executor;
	private final long timeoutMillis;
	private final Runnable task;

	private final AtomicLong delay;
	private final long maxDelay;

	public TimedSupervisorTask(ScheduledExecutorService scheduler, ThreadPoolExecutor executor,
							   int timeout, TimeUnit timeUnit, int expBackOffBound, Runnable task) {
		this.scheduler = scheduler;
		this.executor = executor;
		this.timeoutMillis = timeUnit.toMillis(timeout);
		this.task = task;
		this.delay = new AtomicLong(timeoutMillis);
		this.maxDelay = timeoutMillis * expBackOffBound;
	}

	@Override
	public void run() {
		Future future = null;
		try {
			future = executor.submit(task);
			// block until done or timeout
			future.get(timeoutMillis, TimeUnit.MILLISECONDS);
			delay.set(timeoutMillis);
		} catch (TimeoutException e) {
			logger.error("task supervisor timed out", e);

			long currentDelay = delay.get();
			long newDelay = Math.min(maxDelay, currentDelay * 2);
			delay.compareAndSet(currentDelay, newDelay);
		} catch (RejectedExecutionException e) {
			if (executor.isShutdown() || scheduler.isShutdown()) {
				logger.warn("task supervisor shutting down, reject the task", e);
			} else {
				logger.error("task supervisor rejected the task", e);
			}
		} catch (Throwable e) {
			if (executor.isShutdown() || scheduler.isShutdown()) {
				logger.warn("task supervisor shutting down, can't accept the task");
			} else {
				logger.error("task supervisor threw an exception", e);
			}
		} finally {
			if (future != null) {
				future.cancel(true);
			}
			if (!scheduler.isShutdown()) {
				scheduler.schedule(this, delay.get(), TimeUnit.MILLISECONDS);
			}
		}
	}
}
