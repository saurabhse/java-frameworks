package com.utilities.distributed.scheduler;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
@Profile("shedlock")
public class SchdeulerConfiguration {

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        //MySQL database we are using
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/company");//change url
        dataSource.setUsername("root");//change userid
        dataSource.setPassword("root");//change pwd
        return dataSource;
    }


    /*
* Script to be executed for db-scheduler
*
* CREATE TABLE shedlock(
                         name STRING(64),
                         lock_until TIMESTAMP ,
                         locked_at TIMESTAMP ,
                         locked_by  STRING(255),
                         PRIMARY KEY (name)
) ;
* */
}
