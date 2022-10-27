package com.company.kerberos;

import com.google.common.base.Strings;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@SpringBootApplication
@ConfigurationPropertiesScan
public class KerberosSampleApplication {

    @Autowired
    private Environment environment;

    @Autowired
    private LdapServerProperties ldapServerProperties;

    public static void main(String[] args) {
        SpringApplication.run(KerberosSampleApplication.class, args);
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
        LoggerFactory.getLogger(KerberosSampleApplication.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("local.server.port")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }

    @EventListener
    public void initKeyStore(ApplicationStartedEvent event) {
        if (!Strings.isNullOrEmpty(ldapServerProperties.getKeyStoreFilePath())) {
            System.setProperty("javax.net.ssl.trustStore", ldapServerProperties.getKeyStoreFilePath());
            System.setProperty("javax.net.ssl.trustStorePassword", ldapServerProperties.getKeyStorePassword());
        }
    }
}
