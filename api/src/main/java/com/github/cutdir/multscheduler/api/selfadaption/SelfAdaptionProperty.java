package com.github.cutdir.multscheduler.api.selfadaption;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description: 自适应配置类
 * Demo：
 * scheduler.self.adaption.timeout=2000
 * scheduler.self.adaption.executorThreadPoolSize=5
 * scheduler.self.adaption.expBackOffBound=20
 *
 * @author cutdir
 */
@Getter
@Setter
@ConfigurationProperties(SelfAdaptionProperty.PREFIX)
public class SelfAdaptionProperty {

	public static final String PREFIX = "scheduler.self.adaption";

	/**
	 * 方法超时时间
	 */
	private int timeout = 1000;

	/**
	 * 执行线程maximumPoolSize
	 */
	private int executorThreadPoolSize = 2;

	/**
	 * 最大间隔时间倍数，用来设置最大的方法运行间隔上限
	 */
	private int expBackOffBound = 10;
}
