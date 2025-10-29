package SDD.smash.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "SDD.smash", // 어떤 패키지에서 작동되게 할 것인지
        entityManagerFactoryRef = "dataEntityManager", // 어떤 엔티티 매니저를 사용할 것인지
        transactionManagerRef = "dataTransactionManager" // 어떤 트랜잭션 매니저를 사용할 것인지
)
public class DataDBConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-data")
    public DataSource dataDBSource() {

        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean dataEntityManager() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataDBSource());
        em.setPackagesToScan(new String[]{"SDD.smash"}); // 어떤 패키지에서 엔티티를 찾을 것인지
        em. setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put("hibernate.show_sql", "false");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager dataTransactionManager() {
        // JPA를 위한 트랜잭션 매니저 생성
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(dataEntityManager().getObject());

        return transactionManager;
    }
}
