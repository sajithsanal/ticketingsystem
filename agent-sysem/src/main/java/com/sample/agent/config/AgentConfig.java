package com.sample.agent.config;

import com.oracle.ticketing.config.DBConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class AgentConfig extends DBConfig {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10);
    }






}
