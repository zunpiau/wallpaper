package io.github.zunpiau.config;

import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource({"classpath:jdbc-postgres.properties", "classpath:application.properties"})
public class DataConfig {

    @Bean(name = "jdbcProperties")
    public PropertiesFactoryBean jdbcProperties() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("jdbc-postgres.properties"));
        return bean;
    }

    @Bean
    public DataSourceTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DataSource dataSource(Properties jdbcProperties) throws Exception {
        return new DataSourceFactory().createDataSource(jdbcProperties);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
