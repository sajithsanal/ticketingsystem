package com.oracle.ticketing.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DBConfig {

    @Bean
    public DataSource getReadDataSource() {

        String readHostName = System.getenv().get("DB_READ_HOST");
        String userName = System.getenv().get("DB_READ_USER");
        String password = System.getenv().get("DB_READ_PASSWORD");
        String readPort = System.getenv().get("DB_READ_HOST_PORT");

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://" + readHostName + ":" + readPort + "/ticketing_schema?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSourceBuilder.username(userName);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }

    @Primary
    @Bean
    public DataSource getWriteDataSource() {


        String writeHostName = System.getenv().get("DB_WRITE_HOST");
        String userName = System.getenv().get("DB_WRITE_USER");
        String password = System.getenv().get("DB_WRITE_PASSWORD");
        String writePort = System.getenv().get("DB_WRITE_HOST_PORT");

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://" + writeHostName + ":" + writePort + "/ticketing_schema?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSourceBuilder.username(userName);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }


    @Bean
    public RoutingDataSource getRoutingDataSource(){
        RoutingDataSource routingDataSource = new RoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DbType.MASTER, getWriteDataSource());
        targetDataSources.put(DbType.REPLICA, getReadDataSource());

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(getWriteDataSource());

        return routingDataSource;

    }
}
