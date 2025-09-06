package com.yurakim.readingtrace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "AuditorAwareImpl")
@SpringBootApplication
public class ReadingtraceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadingtraceApplication.class, args);
	}

}