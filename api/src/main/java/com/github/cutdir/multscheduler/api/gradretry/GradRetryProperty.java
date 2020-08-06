package com.github.cutdir.multscheduler.api.gradretry;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description: 梯度重试配置
 * Demo：
 * scheduler.grad.retry.retryTimes=1000,10000,30000
 *
 * @author cutdir
 */

@Getter
@Setter
@ConfigurationProperties(GradRetryProperty.PREFIX)
public class GradRetryProperty {
	public static final String PREFIX = "scheduler.grad.retry";

	/**
	 * 方法超时时间
	 */
	private String retryTimes;
}
