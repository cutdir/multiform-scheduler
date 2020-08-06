# multiform-scheduler 
    多形态的方法调度器,目前提供三种适配，包含自适应、重试和梯度重试。主要依赖ScheduledExecutorService类，对其调度方法进行增强。

### 依赖
    JDK1.8+,maven,springboot应用

### 调度器类型及说明

|   **名称**  |   **类名**  |  **说明**  |
|   :----:   |   :----:    |  :----:  |
|间隔时间自适应调度|SelfAdaptionMode|方法周期性执行,周期间隔按2倍自适应调整|
|重试调度|RetryMode|方法一定时间段内不停尝试执行，直到方法执行成功或到达时间上限|
|梯度重试调度|GradRetryMode|方法一定时间段内按照时间梯度执行，直到方法执行成功或者到达最大梯度次数|

### 接入配置
application.yml如下：

    scheduler:
      self://自适应
        adaption:
          timeout: 2000
      retry://重试
        maxRetryMillis: 3000
      grad://梯度重试
        retry:
          retryTimes: 1000,10000,30000

### 配置类注册
自适应：

    @Bean
    public SelfAdaptionProperty getSelfAdaptionProperty(){
        return new SelfAdaptionProperty();
    }
重试：
          
    @Bean
    public RetryProperty getRetryProperty() {
        return new RetryProperty();
    }
梯度重试：
        
    @Bean
    public GradRetryProperty getGradRetryProperty() {
        return new GradRetryProperty();
    }

### java code
自适应：

    public void printSelfAdaption() {
		SelfAdaptionMode selfAdaptionMode = new SelfAdaptionMode("mySelfAdaption", selfAdaptionProperty, new SelfAdaptionCommand());
		selfAdaptionMode.submitTask();
	}

	class SelfAdaptionCommand implements Runnable {
		@Override
		public void run() {
			log.info("这里是测试SelfAdaptionMode运行,方法执行时间为" + System.currentTimeMillis());
		}
	}	
重试：
	
    public void printRetry() {
		RetryMode retryMode = new RetryMode("myRetry", retryProperty, new RetryCommandImpl());
		retryMode.submitTask();
	}
	class RetryCommandImpl implements Callable {
		@Override
		public Object call() throws Exception {
			log.info("这里是测试RetryMode运行,方法执行时间为" + LocalDateTime.now());
			Thread.sleep(1000);
			throw new Exception("测试抛出异常");
		}
	}
梯度重试

    public void printGradRetry() {
		GradRetryMode gradRetryMode = new GradRetryMode("myGradRetry", gradRetryProperty, new RetryCommandImpl());
		gradRetryMode.submitTask();
	}

	class RetryCommandImpl implements Callable {

		@Override
		public Object call() throws Exception {
			log.info("这里是测试GradRetryMode运行,方法执行时间为" + LocalDateTime.now());
			Thread.sleep(1000);
			throw new Exception("测试抛出异常");
		}
	}
	
### 说明
    简单调用见server模块controller类。