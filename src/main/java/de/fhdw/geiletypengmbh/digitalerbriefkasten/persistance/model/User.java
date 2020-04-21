package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Transient
    private String passwordConfirmation;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    /*
    TODO Add firstname, lastname logic
     */
    private String lastName;

    private String firstName;

    public User() {
    }

    public User(String username, String password, String passwordConfirmation, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
