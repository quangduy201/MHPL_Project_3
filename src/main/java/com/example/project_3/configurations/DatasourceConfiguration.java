package com.example.project_3.configurations;
import com.example.project_3.utils.Resource;
import com.example.project_3.utils.Settings;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.example.project_3.repositories"})
public class DatasourceConfiguration {
    private final Environment env;

    @Autowired
    public DatasourceConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Properties properties = Resource.loadProperties(Settings.DB_FILE, false);
        assert properties != null;
        dataSource.setDriverClassName(properties.getProperty("db.driver"));
        dataSource.setUrl(properties.getProperty("db.url"));
        dataSource.setSchema(properties.getProperty("db.schema"));
        dataSource.setUsername(properties.getProperty("db.username"));
        dataSource.setPassword(properties.getProperty("db.password"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
//        emf.setPackagesToScan(packagesToScan);

        String xmlLocation = Resource.getURLFromResource(Settings.PERSISTENCE_FILE, false).toString();
        assert xmlLocation != null;
        emf.setPersistenceXmlLocation(xmlLocation);

        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
