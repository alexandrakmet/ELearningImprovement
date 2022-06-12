package com.elearning.system.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@PropertySource("classpath:application.properties")
@Configuration
public class DataSourceConfiguration {

    @Autowired
    private Environment environment;

    @Primary
    @Bean
    public DataSource postgresDataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(environment.getProperty("postgres.spring.datasource.driver-class-name"));
        hikariConfig.setJdbcUrl(environment.getProperty("postgres.spring.datasource.url"));
        hikariConfig.setUsername(environment.getProperty("postgres.spring.datasource.username"));
        hikariConfig.setPassword(environment.getProperty("postgres.spring.datasource.password"));
        try {
            hikariConfig.setConnectionTimeout(Integer.parseInt(environment.getProperty("postgres.spring.datasource.hikari.connection-timeout")));
            hikariConfig.setMinimumIdle(Integer.parseInt(environment.getProperty("postgres.spring.datasource.hikari.minimum-idle")));
            hikariConfig.setMaximumPoolSize(Integer.parseInt(environment.getProperty("postgres.spring.datasource.hikari.maximum-pool-size")));
            hikariConfig.setIdleTimeout(Integer.parseInt(environment.getProperty("postgres.spring.datasource.hikari.idle-timeout")));
            hikariConfig.setMaxLifetime(Integer.parseInt(environment.getProperty("postgres.spring.datasource.hikari.max-lifetime")));
        } catch (NullPointerException | NumberFormatException e) {
            return new HikariDataSource(hikariConfig);
        }
        return new HikariDataSource(hikariConfig);
    }

    @Primary
    @Bean
    public JdbcTemplate postgresJdbcTemplate(@Qualifier("postgresDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }


    @Bean
    public DataSource h2DataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(environment.getProperty("h2.spring.datasource.driver-class-name"));
        hikariConfig.setJdbcUrl(environment.getProperty("h2.spring.datasource.url"));
        hikariConfig.setUsername(environment.getProperty("h2.spring.datasource.username"));
        hikariConfig.setPassword(environment.getProperty("h2.spring.datasource.password"));

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public JdbcTemplate h2JdbcTemplate(@Qualifier("h2DataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
