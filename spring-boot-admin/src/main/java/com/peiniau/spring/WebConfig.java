package com.peiniau.spring;

import javax.naming.ServiceUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptorAdapter;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * @see https://github.com/spring-projects/spring-boot/issues/4799<br>
 * @see https://jira.spring.io/browse/SPR-14444<br>
 * @see https://github.com/codecentric/spring-boot-admin/issues/317<br>
 * @see https://github.com/codecentric/spring-boot-admin/issues/236<br>
 *
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	Environment env;
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(env.getProperty("spring.async.default.timeout", Long.class, 3600000L) );
		configurer.registerDeferredResultInterceptors(new DeferredResultProcessingInterceptorAdapter() {
			@Override
			public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> result) {
				result.setErrorResult(new ServiceUnavailableException());
				return false;
			}
		});
	}

}
