package time.geekbang.org.rw.separation.v1.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private static final String DEFAULT_DRIVER_CLASS_NAME = com.mysql.cj.jdbc.Driver.class.getSimpleName();

    private final Environment env;

    public DataSourceConfig(Environment env) {
        this.env = env;
    }

    @Primary
    @Bean
    public DataSource master() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("master.datasource.driver_class_name", DEFAULT_DRIVER_CLASS_NAME));
        dataSource.setUrl(env.getProperty("master.datasource.url"));
        dataSource.setUsername(env.getProperty("master.datasource.username"));
        dataSource.setPassword(env.getProperty("master.datasource.password"));
        return dataSource;

    }

    @Bean
    public DataSource slave1() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("slave1.datasource.driver_class_name", DEFAULT_DRIVER_CLASS_NAME));
        dataSource.setUrl(env.getProperty("slave1.datasource.url"));
        dataSource.setUsername(env.getProperty("slave1.datasource.username"));
        dataSource.setPassword(env.getProperty("slave1.datasource.password"));
        return dataSource;
    }

    @Bean
    public DataSource slave2() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("slave2.datasource.driver_class_name", DEFAULT_DRIVER_CLASS_NAME));
        dataSource.setUrl(env.getProperty("slave2.datasource.url"));
        dataSource.setUsername(env.getProperty("slave2.datasource.username"));
        dataSource.setPassword(env.getProperty("slave2.datasource.password"));
        return dataSource;
    }
}
