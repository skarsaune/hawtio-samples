package no.kantega.example.application.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.jolokia.support.spring.SpringJolokiaAgent;
import org.jolokia.support.spring.SpringJolokiaConfigHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;
import org.springframework.jmx.export.naming.MetadataNamingStrategy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import no.kantega.example.application.dataaccess.CustomerDao;
import no.kantega.example.application.dataaccess.CustomerDaoJdbc;

@Configuration
@EnableTransactionManagement
@ComponentScan("no.kantega.example.application")
public class SpringConfig {


    @Bean(destroyMethod = "close")
    public DataSource dataSource(final HikariConfig config) {
        return new HikariDataSource(config);
    }

    @Bean
    public HikariConfig dataSourceConfig() {
        final HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        config.setUsername("da");
        config.setPassword("");
        config.setMaximumPoolSize(10);
        return config;
    }

    @Bean
    PlatformTransactionManager dataSourceTransactionManager(final DataSource dataSource) {
        return  new DataSourceTransactionManager(dataSource);
    }

    @Bean
    JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean(initMethod = "migrate")
    Flyway flyway(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setBaselineOnMigrate(true);
        flyway.setLocations("migrations");
        return flyway;
    }

    @Bean
    public SpringJolokiaAgent jolokiaAgent(final SpringJolokiaConfigHolder config, final ApplicationContext context) {
        SpringJolokiaAgent jolokiaAgent = new SpringJolokiaAgent();
        jolokiaAgent.setId("jolokiaAgent");
        jolokiaAgent.setExposeApplicationContext(true);// <=== NB! NB!
        jolokiaAgent.setLookupServices(true);
        jolokiaAgent.setConfig(config);
        return jolokiaAgent;
    }

    @Bean
    public SpringJolokiaConfigHolder jolokiaConfig() {
        SpringJolokiaConfigHolder config = new SpringJolokiaConfigHolder();
        HashMap<String, String> configMap = new HashMap<>();
        configMap.put("discoveryEnabled", "true");
        configMap.put("autoStart", "true");
        configMap.put("port", "6789");
        config.setConfig(configMap);
        return config;
    }
    
    @Bean
    public CustomerDao customerDao() {
        return new CustomerDaoJdbc();
    }
    
    
    @Bean
    public MBeanExporter jmxExporter() {
        MBeanExporter exporter = new MBeanExporter();
        AnnotationJmxAttributeSource attributeSource = new AnnotationJmxAttributeSource();
        exporter.setAssembler(new MetadataMBeanInfoAssembler(attributeSource));
        exporter.setNamingStrategy(new MetadataNamingStrategy(attributeSource));
        return exporter;
    }
}
