package com.yurakim.readingtrace.shared.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Configuration
public class DataInitializer {

    private final JdbcTemplate jdbcTemplate;

    @Bean
    public CommandLineRunner initData() {
        return args -> {

            jdbcTemplate.execute("INSERT INTO role (id, name) VALUES (1, 'ROLE_USER') ON DUPLICATE KEY UPDATE name=VALUES(name)");
            jdbcTemplate.execute("INSERT INTO role (id, name) VALUES (2, 'ROLE_ADMIN') ON DUPLICATE KEY UPDATE name=VALUES(name)");

        };
    }
}