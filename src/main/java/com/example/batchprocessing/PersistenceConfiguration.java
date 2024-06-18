package com.example.batchprocessing;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class PersistenceConfiguration {

    @Value("${db.driverclassname}")
    private String driverclassname;
    @Value("${db.url}")
    private String url;
    @Value("${db.password}")
    private String password;
    @Value("${db.username}")
    private String username;

    @Bean
    public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public DataSourceProperties batchDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDatabaseType(DatabaseType.H2.getProductName());
        factory.setDataSource(dataSource());
        factory.setTransactionManager(transactionManager());
        return factory.getObject();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        var source = new DriverManagerDataSource();
        source.setDriverClassName(driverclassname);
        source.setUrl(url); // In case not windows when using in file mode: https://www.h2database.com/html/features.html#database_url
        source.setPassword(username);
        source.setUsername(password);
        return source;
    }
}
