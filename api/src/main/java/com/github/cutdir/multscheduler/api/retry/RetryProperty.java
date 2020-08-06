package com.github.cutdir.multscheduler.api.retry;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description: 重试配置
 * Demo：
 * scheduler.retry.maxRetryMillis=3000
 *
 * @author cutdir
 */

@Getter
@Setter
@ConfigurationProperties(RetryProperty.PREFIX)
public class RetryProperty {
	public static final String PREFIX = "scheduler.retry";

	/**
	 * 方法超时时间
	 */
	private int maxRetryMillis = 1000;
}
