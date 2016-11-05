package com.peiniau.spring;

import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
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
			public <T> boolean handleTimeout(NativeWebRequest nativeWebRequest, DeferredResult<T> result) {
				HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
				StringBuilder msg = new StringBuilder();
				msg.append("uri=").append(request.getRequestURI());
				String queryString = request.getQueryString();
				if (queryString != null) {
					msg.append('?').append(queryString);
				}
				String client = request.getRemoteAddr();
				if (StringUtils.hasLength(client)) {
					msg.append(";client=").append(client);
				}
				HttpSession session = request.getSession(false);
				if (session != null) {
					msg.append(";session=").append(session.getId());
				}
				String user = request.getRemoteUser();
				if (user != null) {
					msg.append(";user=").append(user);
				}
				msg.append(";headers=").append(new ServletServerHttpRequest(request).getHeaders());
				
				result.setErrorResult(new ServiceUnavailableException("request: " + msg.toString()));
				return false;
			}
		});
	}

}
