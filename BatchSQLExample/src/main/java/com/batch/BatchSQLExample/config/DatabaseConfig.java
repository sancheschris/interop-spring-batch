package com.batch.BatchSQLExample.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@Log4j2
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String jdbcURl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;



    @Bean(name = "dataSource")
    public DataSource dataSource() throws SQLException {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(jdbcURl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        log.info("Connected to the datebase {}", dataSource.getConnection() );

        return dataSource;
    }

}
