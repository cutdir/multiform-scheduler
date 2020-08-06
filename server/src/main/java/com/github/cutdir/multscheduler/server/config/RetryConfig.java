package com.github.cutdir.multscheduler.server.config;

import com.github.cutdir.multscheduler.api.retry.RetryProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: RetryConfig
 *
 * @author cutdir
 */
@Configuration
public class RetryConfig {

	@Bean
	public RetryProperty getRetryProperty() {
		return new RetryProperty();
	}
}
