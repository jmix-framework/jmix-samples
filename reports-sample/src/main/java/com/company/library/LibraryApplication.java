package com.company.library;

import com.company.library.app.ReportHistoryCleanJob;
import com.google.common.base.Strings;
import org.quartz.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@SpringBootApplication
public class LibraryApplication {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource.hikari")
    DataSource dataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @EventListener
    public void printApplicationUrl(ApplicationStartedEvent event) {
        LoggerFactory.getLogger(LibraryApplication.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("local.server.port")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }

    // tag::quartz[]
    @Bean
    JobDetail reportHistoryCleanJob() {
        return JobBuilder.newJob()
                .ofType(ReportHistoryCleanJob.class)
                .storeDurably()
                .withIdentity("reportHistoryClean")
                .build();
    }

    @Bean
    Trigger reportHistoryCleanTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(reportHistoryCleanJob())
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 * * ?")) // <1>
                .build();
    }
    // end::quartz[]
}
