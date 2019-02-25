package org.luncert.csdn2.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 配置一个数据源，需要配置{@code ManagerFactoryRef}实体关联管理工厂类
 * 和{@code TransactionManagerRef}事务管理类
 * https://blog.csdn.net/mickjoust/article/details/80352795
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "org.luncert.csdn2.repository.mongo",
    entityManagerFactoryRef = "manEntityManagerFactory",
    transactionManagerRef = "manTransactionManager"
)
@EnableTransactionManagement
public class MongoDBConfig
{
    // 属性管理类，这里主要用来引入一些系统环境属性
    @Autowired
    private Environment env;

    /**
     * 定义前缀，并返回DataSourceProperties对象
     * 自动配置时加载的对象DataSourceAutoConfiguration会定义它，
     * 并通过它读取配置的内容，这里new个新对象就行了，
     * 主要是为后面的bean初始化服务。 
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.data.mongodb")
    public DataSourceProperties mongoDSProperties() {
        return new DataSourceProperties();
    }
    
    /**
     * 获取前缀的DataSourceProperties对象，并创建真正的DataSource数据源,
     * 这里我们使用的是Spring Boot自带的工具类DataSourceBuilder，值来源
     * 的就是从前缀对象中读取的值，就是在配置文件里我们写的值
     */
    @Bean
    public DataSource mongoDataSource() {
        DataSourceProperties dataSourceProperties = mongoDSProperties();
        return DataSourceBuilder.create()
            .driverClassName(dataSourceProperties.getDriverClassName())
            .url(dataSourceProperties.getUrl())
            .username(dataSourceProperties.getUsername())
            .password(dataSourceProperties.getPassword())
            .build();
    }

    /**
     * 事物管理器的主接口PlatformTransactionManager需要获取到
     * JpaTransactionManager的对象进行事务管理，这个对象就是
     * 由{@link #mongoEntityManagerFactory()}创建的
     */
    @Bean
    public PlatformTransactionManager mongoTransactionManager() {
        return new JpaTransactionManager(
            mongoEntityManagerFactory().getObject());
    }

    /**
     * 这是JPA的开发规范，的确很麻烦，这是为了简单而做出的牺牲，
     * 简单说，这里会将我们的数据源，适配器，配置策略都全部封装好
     * 返回，让{@link #mongoTransactionManager()}调用来创建
     * 返回给事务管理器。而同时，Boot会通过加载配置属性来获取配置值
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean mongoEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(mongoDataSource());
        factory.setPackagesToScan("org.luncert.csdn2.repository.mongo");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("hibernate.show-sql"));

        factory.setJpaProperties(jpaProperties);
        return factory;
    }

}