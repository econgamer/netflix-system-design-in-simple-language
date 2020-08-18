package com.example.circuitbreakertester;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

//From Tutorial: https://www.baeldung.com/introduction-to-hystrix
public class HystrixTimeoutManualTest {
	
	@Test
    public void givenInputBobAndDefaultSettings_whenCommandExecuted_thenReturnHelloBob() {
        assertThat(new CommandHelloWorld("Bob").execute(), equalTo("Hello Bob!"));
    }
	
	@Test
	public void givenSvcTimeoutOf100AndDefaultSettings_whenRemoteSvcExecuted_thenReturnSuccess() throws InterruptedException{
		HystrixCommand.Setter config = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroup2"));
		
		assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(100)).execute(), equalTo("Success"));
	}
	
	
	@Test
	public void givenSvcTimeoutOf500AndExecTimeoutOf10000_whenRemoteSvcExecuted_thenReturnSuccess() throws InterruptedException{
		HystrixCommand.Setter config = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest4"));
		
		HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
		commandProperties.withExecutionTimeoutInMilliseconds(10_000);
		config.andCommandPropertiesDefaults(commandProperties);
		
		assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(), equalTo("Success"));
	}
	
	//This demonstrates how Hystrix does not wait longer than the configured timeout for a response. 
	// This helps make the system protected by Hystrix more responsive.
	
	@Test
	public void givenSvcTimeoutOf15000AndExecTimeoutOf5000_whenRemoteSvcExecuted_thenExpectHre() throws InterruptedException {
		
		HystrixCommand.Setter config = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest5"));
		
		HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
		commandProperties.withExecutionTimeoutInMilliseconds(5_000);
		config.andCommandPropertiesDefaults(commandProperties);
		
		
		
		Assertions.assertThrows(HystrixRuntimeException.class, () -> new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(15_000)).execute());
	}
	
	
	@Test
	public void givenSvcTimeoutOf500AndExecTimeout10000AndThreadPool_whenRemoteSvcExecuted_thenReturnSuccess() throws InterruptedException{
		
		HystrixCommand.Setter config = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupThreadPool"));
		
		HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
		commandProperties.withExecutionTimeoutInMilliseconds(10_000);
		config.andCommandPropertiesDefaults(commandProperties);
		config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
				.withMaxQueueSize(1)
				.withCoreSize(1)
				.withQueueSizeRejectionThreshold(1));
		
		assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(), 
				equalTo("Success"));
		
		assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(), 
				equalTo("Success"));
		
		assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(), 
				equalTo("Success"));
		
	}
	
//	In the above test, we are setting the maximum queue size, 
//	the core queue size and the queue rejection size. Hystrix will start rejecting the requests when the maximum number 
//	of threads have reached 1 and the task queue has reached a size of 1.
	
	
	@Test
	public void givenCircuitBreakerSetup_whenRemoteSvcCmdExecuted_thenReturnSuccess() throws InterruptedException {
		

		HystrixCommand.Setter config = HystrixCommand
			      .Setter
			      .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupCircuitBreaker"));
			 
			    HystrixCommandProperties.Setter properties = HystrixCommandProperties.Setter();
			    properties.withExecutionTimeoutInMilliseconds(100);
			    properties.withCircuitBreakerSleepWindowInMilliseconds(4000);
			    properties.withExecutionIsolationStrategy
			     (HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
			    properties.withCircuitBreakerEnabled(true);
			    properties.withCircuitBreakerRequestVolumeThreshold(1);
			 
			    config.andCommandPropertiesDefaults(properties);
			    config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
			      .withMaxQueueSize(1)
			      .withCoreSize(1)
			      .withQueueSizeRejectionThreshold(1));
			 
			    assertThat(this.invokeRemoteService(config, 100_000), equalTo(null));
			    assertThat(this.invokeRemoteService(config, 100_000), equalTo(null));
			    assertThat(this.invokeRemoteService(config, 100_000), equalTo(null));
			 
			    //Thread.sleep(5000);
			 
			    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
			      equalTo("Success"));
			 
			    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
			      equalTo("Success"));
			 
			    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
			      equalTo("Success"));
		
		
		
//		HystrixCommand.Setter config = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupCircuitBreaker"));
//		
//		HystrixCommandProperties.Setter properties = HystrixCommandProperties.Setter();
//		properties.withExecutionTimeoutInMilliseconds(1000);
//		//properties.withCircuitBreakerSleepWindowInMilliseconds(4000);
//		//properties.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
//		//properties.withCircuitBreakerEnabled(true);
//		//properties.withCircuitBreakerRequestVolumeThreshold(1);
//		
//		config.andCommandPropertiesDefaults(properties);
//		config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
//				.withMaxQueueSize(1)
//				.withCoreSize(1)
//				.withQueueSizeRejectionThreshold(1));
//		
//		 assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));
//		 assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));
//		 assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));
//			 
////	    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(10_000)).execute(),
////	      equalTo("Success"));
////	 
////	    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
////	      equalTo("Success"));
////		
//		Thread.sleep(5000);
//		 
//	    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
//	      equalTo("Success"));
//	 
//	    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
//	      equalTo("Success"));
//	 
//	    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
//	      equalTo("Success"));
		
		
	}
	
	
	
	public String invokeRemoteService(HystrixCommand.Setter config, long timeout)
			  throws InterruptedException {
			 
			    String response = null;
			 
			    try {
			        response = new RemoteServiceTestCommand(config,
			          new RemoteServiceTestSimulator(timeout)).execute();
			    } catch (HystrixRuntimeException ex) {
			        System.out.println("ex = " + ex);
			    }
			 
			    return response;
			}

}




