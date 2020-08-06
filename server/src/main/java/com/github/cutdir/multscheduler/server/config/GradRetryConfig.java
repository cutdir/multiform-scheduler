package com.github.cutdir.multscheduler.server.config;

import com.github.cutdir.multscheduler.api.gradretry.GradRetryProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: GradRetryConfig
 *
 * @author cutdir
 */
@Configuration
public class GradRetryConfig {

	@Bean
	public GradRetryProperty getGradRetryProperty() {
		return new GradRetryProperty();
	}
}
