package ru.davydenko.spring.rest.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration // Помечаем, что класс является конфигурационным
@ComponentScan(basePackages = "ru.davydenko.spring.rest") // Прописываем каталог на который распрстроняется наш конфиг файл
@EnableWebMvc
@PropertySource("classpath:application.properties")
@EnableTransactionManagement //Spring будет автоматически управлять транзакциями для всех методов, помеченных аннотацией @Transactional
public class MyConfig {

    @Value("${db.userName}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    //Создание подключения к БД
    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("org.postgresql.Driver");
            dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/my_db");
            dataSource.setUser(dbUser);
            dataSource.setPassword(dbPassword);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    //Создание sessionFactory
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("ru.davydenko.spring.rest.entity");

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
    }

    //управляет транзакциями для Hibernate.
    //обеспечивает поддержку транзакций в приложении, когда работаем с базой данных через Hibernate.
    @Bean
    public HibernateTransactionManager transactionManager(){
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}
