package com.integravida.IntegraVidaBackend.iam.infrastructure.hashing;

import org.springframework.stereotype.Service;

@Service
public class DefaultPasswordEncoder {
    // Basic placeholder for a password encoder since spring-security is not inherently present based on search.
    // Replace with BCryptPasswordEncoder when spring-security is added to pom.xml
    public String encode(String rawPassword) {
        // En un entorno de producción, esto debería usar un hash fuerte (e.g. BCrypt)
        // Por ahora, o bien se agrega spring security, o simulamos o agregamos una librería como jbcrypt
        return "hashed_" + rawPassword; // Dummy implementation until library is defined
    }
}
