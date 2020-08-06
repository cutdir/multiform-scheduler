package com.github.cutdir.multscheduler.server.config;

import com.github.cutdir.multscheduler.api.selfadaption.SelfAdaptionProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: SelfAdaptionConfig
 *
 * @author cutdir
 */
@Configuration
public class SelfAdaptionConfig {

	@Bean
	public SelfAdaptionProperty getSelfAdaptionProperty(){
		return new SelfAdaptionProperty();
	}
}
