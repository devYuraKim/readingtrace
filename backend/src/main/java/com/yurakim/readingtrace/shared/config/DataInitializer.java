package com.yurakim.readingtrace.shared.config;

import com.yurakim.readingtrace.auth.enums.RoleType;
import com.yurakim.readingtrace.shelf.enums.DefaultShelfType;
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

            //ROLE table seeding (idempotent)
            for (RoleType role : RoleType.values()) {
                jdbcTemplate.update(
                        "INSERT INTO role (name) VALUES (?) ON DUPLICATE KEY UPDATE name=VALUES(name)",
                        role.name()
                );
            }

            //DEFAULT_SHELF seeding (idempotent)
            for (DefaultShelfType type : DefaultShelfType.values()) {
                jdbcTemplate.update(
                    """
                        INSERT INTO default_shelf (name, slug, orderIndex) 
                        VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE name=VALUES(name)
                        """,
                        type.getName(), type.getSlug(), type.getOrderIndex()
                );
            }

        };
    }
}