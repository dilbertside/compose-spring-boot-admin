/**
 * NotifierConfig
 */
package com.peiniau.spring;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import de.codecentric.boot.admin.notify.LoggingNotifier;
import de.codecentric.boot.admin.notify.Notifier;
import de.codecentric.boot.admin.notify.RemindingNotifier;
import de.codecentric.boot.admin.notify.filter.FilteringNotifier;

/**
 * @author lch on Nov 3, 2016 12:58:21 PM
 * @since 1.0.0
 * @version 1.0.0
 *
 */
@Configuration
@EnableScheduling
public class NotifierConfig {
	
	private String[] reminderStatuses = { "DOWN", "OFFLINE" , "UNKNOWN", "UP"};

	@Bean
  public FilteringNotifier filteringNotifier(Notifier delegate) { 
    return new FilteringNotifier(delegate);
  }
	
	@Bean
	@Primary
	public RemindingNotifier remindingNotifier() {
		RemindingNotifier remindingNotifier = new RemindingNotifier(filteringNotifier(loggerNotifier()));
		remindingNotifier.setReminderPeriod(TimeUnit.MINUTES.toMillis(1));//The reminders will be sent every minute.
		remindingNotifier.setReminderStatuses(reminderStatuses);
		return remindingNotifier;
	}

	//Schedules sending of due reminders every 60 seconds.
	@Scheduled(fixedRate = 60000L)
	public void remind() {
		remindingNotifier().sendReminders();
	}

	@Bean
	public LoggingNotifier loggerNotifier() {
		return new LoggingNotifier();
	}
}
