package com.integravida.IntegraVidaBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IntegrateVidaBackendApplication {

	public static void main(String[] args) {
		System.out.println("USUARIO DETECTADO: " + System.getenv("SPRING_DATASOURCE_USERNAME"));
		System.out.println("PASSWORD DETECTADA: " + System.getenv("SPRING_DATASOURCE_PASSWORD"));
		SpringApplication.run(IntegrateVidaBackendApplication.class, args);
	}+
}
