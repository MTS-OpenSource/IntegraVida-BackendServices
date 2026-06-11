package com.integravida.IntegraVidaBackend.profiles.domain.model.aggregates;

import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.DateOfBirth;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.EmailAddress;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.PersonName;
import com.integravida.IntegraVidaBackend.profiles.domain.model.valueobjects.PhoneNumber;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Profile Aggregate — Profiles Bounded Context
 *
 * Manages the personal data of a user (patient or doctor).
 * Encapsulates identity information such as name, email,
 * phone number and date of birth.
 *
 * Publishes ProfileCreatedEvent upon creation, which is
 * consumed by the patients and doctors bounded contexts.
 */
@Entity
@Table(name = "profiles")
@Getter
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "first_name", nullable = false)),
        @AttributeOverride(name = "lastName",  column = @Column(name = "last_name",  nullable = false))
    })
    private PersonName name;

    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "email", nullable = false, unique = true))
    private EmailAddress email;

    @Embedded
    @AttributeOverride(name = "number", column = @Column(name = "phone_number"))
    private PhoneNumber phoneNumber;

    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "date_of_birth"))
    private DateOfBirth dateOfBirth;

    /**
     * Creates a new Profile.
     * After calling this constructor, the caller should publish a ProfileCreatedEvent.
     */
    public Profile(String firstName, String lastName, String email,
                   String phoneNumber, LocalDate dateOfBirth) {
        this.name        = new PersonName(firstName, lastName);
        this.email       = new EmailAddress(email);
        this.phoneNumber = new PhoneNumber(phoneNumber);
        this.dateOfBirth = new DateOfBirth(dateOfBirth);
    }

    // ── Domain behaviour ────────────────────────────────────

    public void updateName(String firstName, String lastName) {
        this.name = new PersonName(firstName, lastName);
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = new PhoneNumber(phoneNumber);
    }

    public void updateDateOfBirth(LocalDate date) {
        this.dateOfBirth = new DateOfBirth(date);
    }

    // ── Convenience getters ─────────────────────────────────

    public String getFullName()      { return name.getFullName(); }
    public String getEmailAddress()  { return email.getEmail(); }
    public String getPhoneNumberStr(){ return phoneNumber.getNumber(); }
    public LocalDate getBirthDate()  { return dateOfBirth.getDate(); }
}
