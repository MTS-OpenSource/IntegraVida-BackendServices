package com.integravida.IntegraVidaBackend.iam.domain.model;

import com.integravida.IntegraVidaBackend.iam.domain.model.valueobjects.Password;
import com.integravida.IntegraVidaBackend.iam.domain.model.valueobjects.Username;
import com.integravida.IntegraVidaBackend.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User extends AbstractDomainAggregateRoot<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Username username;

    @Embedded
    private Password password;

    @Column(nullable = false, unique = true)
    private String email;

    @Convert(converter = RoleConverter.class)
    @Column(nullable = false, length = 50)
    private Roles role;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public User() {
        // Default constructor for JPA
    }

    public User(Username username, Password password, String email, Roles role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String username, String password, String email, Roles role) {
        this(new Username(username), new Password(password), email, role);
    }

    public static User createPatient(String username, String password, String email) {
        return new User(username, password, email, Roles.PATIENT);
    }

    public static User createDoctor(String username, String password, String email) {
        return new User(username, password, email, Roles.DOCTOR);
    }

    public static User createAdmin(String username, String password, String email) {
        return new User(username, password, email, Roles.ADMIN);
    }

    public Long getId() {
        return id;
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Roles getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
