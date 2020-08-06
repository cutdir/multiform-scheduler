package com.github.cutdir.multscheduler.api.retry;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description: InterruptTask类通过scheduler在指定delay时间后执行run方法，给指定线程打中断标记
 *
 * @author cutdir
 */
public class InterruptTask extends TimerTask {

	protected Thread target;

	public InterruptTask(ScheduledExecutorService scheduler, long millis) {
		target = Thread.currentThread();
		scheduler.schedule(this, millis, TimeUnit.MILLISECONDS);
	}


	public InterruptTask(ScheduledExecutorService scheduler, Thread target, long millis) {
		this.target = target;
		scheduler.schedule(this, millis, TimeUnit.MILLISECONDS);
	}


	@Override
	public boolean cancel() {
		try {
			return super.cancel();
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void run() {
		if ((target != null) && (target.isAlive())) {
			target.interrupt();
		}
	}
} 