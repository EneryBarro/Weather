package com.sc.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @JsonIgnore
    private String id;
    @JsonProperty("Login")
    private String login;
    @JsonProperty("First name")
    @Column(name = "first_name")
    private String firstName;
    @JsonProperty("Surname")
    private String surname;
    @JsonProperty("Email")
    private String email;
    @Column(name = "phone_number")
    @JsonProperty("Phone number")
    private String phoneNumber;
    @JsonProperty("Country")
    private String country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;

        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
