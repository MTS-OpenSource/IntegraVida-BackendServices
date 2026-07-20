package com.integravida.IntegraVidaBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IntegrateVidaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegrateVidaBackendApplication.class, args);
	}
}
