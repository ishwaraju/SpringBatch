package com.ishwaraju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class VoltBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoltBatchApplication.class, args);
	}

}
